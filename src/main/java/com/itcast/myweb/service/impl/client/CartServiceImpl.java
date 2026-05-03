package com.itcast.myweb.service.impl.client;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.exception.*;
import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.common.pojo.PageSearch;
import com.itcast.myweb.domain.dto.CartDTO;
import com.itcast.myweb.domain.dto.UserDTO;
import com.itcast.myweb.domain.entity.ItemBase;
import com.itcast.myweb.domain.entity.ItemSku;
import com.itcast.myweb.domain.entity.ShoppingCart;
import com.itcast.myweb.domain.vo.CartVO;
import com.itcast.myweb.domain.vo.ItemBaseVO;
import com.itcast.myweb.domain.vo.ItemDetailVO;
import com.itcast.myweb.domain.vo.ItemSkuVO;
import com.itcast.myweb.enums.ItemStatus;
import com.itcast.myweb.service.client.CartService;
import com.itcast.myweb.service.common.IItemBaseService;
import com.itcast.myweb.service.common.IItemSkuService;
import com.itcast.myweb.service.common.IShoppingCartService;
import com.itcast.myweb.utils.TTLOffset;
import com.itcast.myweb.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * 购物车服务实现类
 */
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    // 购物车服务
    private final IShoppingCartService shoppingCartService;
    // redis服务
    private final StringRedisTemplate redisTemplate;
    // sku服务
    private final IItemSkuService itemSkuService;
    // spu服务
    private final IItemBaseService itemBaseService;


    /**
     * 添加购物车
     */
    @Override
    public void addCart(CartDTO cartDTO) {

        //准备数据
        Long skuId = cartDTO.getSkuId();
        Long baseId = cartDTO.getBaseId();
        Integer num = cartDTO.getNum();
        UserDTO userDTO = UserHolder.get();
        Long userId = userDTO.getId();

        //构建购物车数据
        ShoppingCart shoppingCart = new ShoppingCart();

        //1.查询缓存中是否存在
        Set<String> members = redisTemplate.opsForSet().members(Constant.ITEM_SKU_CACHE_KEY_PREFIX + baseId);

        //判断是否为空
        //2.存在，构建购物车数据
        if (members != null && !members.isEmpty()) {
            //转为ItemSkuVO
            List<ItemSkuVO> itemSkuVOList = members.stream()
                    .map(json -> JSONUtil.toBean(json, ItemSkuVO.class))
                    .collect(Collectors.toList());
            //查询是否有匹配的sku
            List<ItemSkuVO> itemSkuVOS = itemSkuVOList.stream().filter(itemSkuVO -> itemSkuVO.getId().equals(skuId)).collect(Collectors.toList());
            if (!itemSkuVOS.isEmpty()) {
                ItemSkuVO itemSkuVO = itemSkuVOS.get(0);
                //拷贝到购物车
                BeanUtil.copyProperties(itemSkuVO, shoppingCart);
                //设置数量
                shoppingCart.setNum(num);
                //设置用户id
                shoppingCart.setUserId(userId);
                //设置skuId
                shoppingCart.setSkuId(skuId);
                //取消id
                shoppingCart.setId(null);
            }
        }

        //3.不存在，查询数据库
        ItemSku itemSku = itemSkuService.lambdaQuery()
                .eq(ItemSku::getId, skuId)
                .one();
        ItemSkuVO itemSkuVO = new ItemSkuVO();
        //判断是否为空
        if (itemSku == null) {
            //商品不存在
            throw new ItemDoesntExistException("商品不存在");
        }

        //4.构建购物车数据
        //拷贝到ItemSkuVO
        BeanUtil.copyProperties(itemSku, itemSkuVO);
        BeanUtil.copyProperties(itemSkuVO, shoppingCart);
        shoppingCart.setUserId(userId);
        shoppingCart.setNum(num);
        shoppingCart.setSkuId(skuId);
        shoppingCart.setId(null);

        //5.写入数据库
        shoppingCartService.saveOrUpdate(shoppingCart);


    }


    /**
     * 分页查询购物车
     */
    @Override
    public PageResult<CartVO> pageList(PageSearch pageSearch) {

        //0.准备数据
        Long pageNo = pageSearch.getPageNo();
        Long pageSize = pageSearch.getPageSize();

        //1.获取当前用户的id
        UserDTO userDTO = UserHolder.get();
        Long userId = userDTO.getId();

        //2.分页查询
        //判断每页数量是否为null
        if (pageSize == null) {
            //设置为默认值
            pageSize = Constant.CART_PAGE_SIZE;
        }

        //分页条件
        Page<ShoppingCart> page = new Page<>(pageNo, pageSize);
        shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getUserId, userId)
                .orderByDesc(ShoppingCart::getCreateTime)
                .page(page);


        //3.转为VO
        List<CartVO> cartVOList = page.getRecords().stream()
                .map(shoppingCart -> {
                    return BeanUtil.toBean(shoppingCart, CartVO.class);
                })
                .collect(Collectors.toList());


        //4.获取最新商品数据

        //获取商品id
        List<Long> skuIdList = cartVOList.stream().map(CartVO::getSkuId).collect(Collectors.toList());

        //批量查询商品数据
        List<ItemSku> itemSkuList = itemSkuService.lambdaQuery()
                .in(ItemSku::getId, skuIdList)
                .list();

        //商品数据转为以id为键的Map
        Map<Long, ItemSku> itemSkuMap = itemSkuList.stream()
                .collect(Collectors.toMap(ItemSku::getId, itemSku -> itemSku));


        //补充CartVO数据
        for (CartVO cartVO : cartVOList) {
            ItemSku itemSku = itemSkuMap.get(cartVO.getSkuId());
            //判断是否为空
            if (itemSku == null) {
                continue;
            }
            cartVO.setNewPrice(itemSku.getPrice());
            cartVO.setStock(itemSku.getStock());
            //判断商品状态
            // TODO 根据店铺id查询店铺状态
            //判断商品是否下架
            if (!itemSku.getIsOnSale()) {
                cartVO.setStatus(ItemStatus.ITEM_OFF_SHELF);
            }
            //判断商品是否缺货
            if (itemSku.getStock() <= cartVO.getNum()) {
                cartVO.setStatus(ItemStatus.STYLE_OUT_OF_STOCK);
            }
        }


        //5.组装返回数据
        return PageResult.<CartVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(cartVOList)
                .build();
    }


    /**
     * 修改购物车数量
     */
    @Override
    public void updateNum(CartDTO cartDTO) {

        //0.准备数据
        Long id = cartDTO.getId();
        Long uid = UserHolder.get().getId();
        Integer num = cartDTO.getNum();

        //1.根据id查询购物车
        ShoppingCart shoppingCart = shoppingCartService.getById(id);

        //判断购物车是否存在
        if (shoppingCart == null) {
            throw new CartDoesntExistException("购物车不存在");
        }
        //判断用户id是否一致
        if (!shoppingCart.getUserId().equals(uid)) {
            throw new UserIdDoesntMatchException("用户id不一致");
        }
        //判断数量是否超过最大值
        if (num > Constant.CART_MAX_NUM) {
            throw new CartNumExceedMaxException("数量超过最大值");
        }


        //2.修改数量
        shoppingCartService.lambdaUpdate()
                .eq(ShoppingCart::getId, id)
                .eq(ShoppingCart::getUserId, uid)
                .set(ShoppingCart::getNum, num)
                .update();


        //3.返回


    }


    /**
     * 删除购物车
     */
    @Override
    public void deleteCart(Long id) {

        //0.准备数据
        Long uid = UserHolder.get().getId();


        //1.根据id查询购物车
        ShoppingCart shoppingCart = shoppingCartService.getById(id);

        //判断购物车是否存在
        if (shoppingCart == null) {
            throw new CartDoesntExistException("购物车不存在");
        }
        //判断用户id是否一致
        if (!shoppingCart.getUserId().equals(uid)) {
            throw new UserIdDoesntMatchException("用户id不一致");
        }


        //2.删除购物车
        shoppingCartService.removeById(id);


        //3.返回


    }


    /**
     * 进入商品详情页
     */
    @Override
    public ItemDetailVO detail(Long id) {

        //0.准备数据
        Long uid = UserHolder.get().getId();

        //查询购物车
        ShoppingCart cart = shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getId, id)
                .one();

        //判断购物车是否存在
        if (cart == null) {
            throw new CartDoesntExistException("购物车不存在");
        }
        //判断用户id是否一致
        if (!cart.getUserId().equals(uid)) {
            throw new UserIdDoesntMatchException("用户id不一致");
        }

        Long skuId = cart.getSkuId();
        Long baseId = cart.getBaseId();


        ItemDetailVO itemDetailVO = new ItemDetailVO();
        //1.根据base_id查询商品基础信息
        //查询缓存
        ItemBaseVO itemBaseVO = null;
        String itemBaseVOJson = redisTemplate.opsForValue().get(Constant.ITEM_BASE_CACHE_KEY_PREFIX + baseId);
        //判断缓存是否为空
        if (itemBaseVOJson == null) {
            //查询数据库
            ItemBase itemBase = itemBaseService.getById(baseId);
            //判断商品基础信息是否存在
            if (itemBase == null) {
                throw new ItemDoesntExistException("商品不存在");
            }
            //转为ItemBaseVO
            itemBaseVO = BeanUtil.toBean(itemBase, ItemBaseVO.class);
            //缓存商品基础信息
            redisTemplate.opsForValue().set(Constant.ITEM_BASE_CACHE_KEY_PREFIX + baseId, JSONUtil.toJsonStr(itemBaseVO), TTLOffset.getRandomTTL(Constant.ITEM_BASE_CACHE_TTL), TimeUnit.MINUTES);
        } else {
            //转为ItemBaseVO
            itemBaseVO = JSONUtil.toBean(itemBaseVOJson, ItemBaseVO.class);
        }


        //2.根据sku_id查询商品sku信息
        //查询缓存
        List<ItemSkuVO> itemSkuVOList = null;
        Set<String> members = redisTemplate.opsForSet().members(Constant.ITEM_SKU_CACHE_KEY_PREFIX + baseId);
        //判断缓存是否为空
        if (members == null || members.isEmpty()) {
            //查询数据库
            List<ItemSku> list = itemSkuService.lambdaQuery()
                    .eq(ItemSku::getBaseId, baseId)
                    .eq(ItemSku::getIsOnSale, Boolean.TRUE)
                    .list();
            //判断商品sku列表是否为空
            if (list == null || list.isEmpty()) {
                throw new ItemSkuDoesntExistException("该规格不存在");
            }
            //转为ItemSkuVO
            itemSkuVOList = list.stream()
                    .map(itemSku -> {
                        return BeanUtil.toBean(itemSku, ItemSkuVO.class);
                    })
                    .collect(Collectors.toList());
            //缓存商品sku列表
            redisTemplate.opsForSet().add(Constant.ITEM_SKU_CACHE_KEY_PREFIX + baseId,
                    itemSkuVOList.stream()
                            .map(JSONUtil::toJsonStr)
                            .toArray(String[]::new));
            //设置过期时间
            redisTemplate.expire(Constant.ITEM_SKU_CACHE_KEY_PREFIX + baseId, TTLOffset.getRandomTTL(Constant.ITEM_SKU_CACHE_TTL), TimeUnit.MINUTES);
        } else {
            //转为ItemSkuVO
            itemSkuVOList = members.stream()
                    .map(json -> JSONUtil.toBean(json, ItemSkuVO.class))
                    .collect(Collectors.toList());
        }


        //3.拼接返回
        BeanUtil.copyProperties(itemBaseVO, itemDetailVO);
        itemDetailVO.setSkuVOList(itemSkuVOList);

        return itemDetailVO;


    }
}
