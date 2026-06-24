package com.itcast.myweb.service.impl.client;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.exception.*;
import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.domain.dto.CartDTO;
import com.itcast.myweb.domain.dto.CartPageDTO;
import com.itcast.myweb.domain.dto.UserDTO;
import com.itcast.myweb.domain.entity.Address;
import com.itcast.myweb.domain.entity.ItemBase;
import com.itcast.myweb.domain.entity.ItemSku;
import com.itcast.myweb.domain.entity.ShoppingCart;
import com.itcast.myweb.domain.vo.CartVO;
import com.itcast.myweb.domain.vo.ItemBaseVO;
import com.itcast.myweb.domain.vo.ItemDetailVO;
import com.itcast.myweb.domain.vo.ItemSkuVO;
import com.itcast.myweb.enums.ItemStatus;
import com.itcast.myweb.service.client.CartService;
import com.itcast.myweb.service.client.ItemService;
import com.itcast.myweb.service.common.*;
import com.itcast.myweb.utils.OrderItemUtils;
import com.itcast.myweb.utils.TTLOffset;
import com.itcast.myweb.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    // 地址服务
    private final IAddressService addressService;
    // item服务
    private final ItemServiceImpl itemService;


    /**
     * 添加购物车
     */
    @Override
    public void addCart(CartDTO cartDTO) {

        //准备数据
        Long skuId = cartDTO.getSkuId();
        Integer num = cartDTO.getNum();
        UserDTO userDTO = UserHolder.get();
        Long userId = userDTO.getId();


        //构建购物车数据
        ShoppingCart shoppingCart = new ShoppingCart();

        //1.查询缓存中是否存在
        String jsonObj = redisTemplate.opsForValue().get(Constant.ITEM_SKU_CACHE_KEY_PREFIX + skuId);


        ItemSku itemSku = new ItemSku();
        //判断是否为空
        //2.存在，构建购物车数据
        if (jsonObj != null) {
            //转为ItemSku
            itemSku = JSONUtil.toBean(JSONUtil.toJsonStr(jsonObj), ItemSku.class);
        } else {
            //3.不存在，查询数据库
            itemSku = itemSkuService.lambdaQuery()
                    .eq(ItemSku::getId, skuId)
                    .one();
            //判断是否为空
            if (itemSku == null) {
                throw new ItemDoesntExistException("商品不存在");
            }
        }


        //4.构建购物车数据
        BeanUtil.copyProperties(itemSku, shoppingCart);
        shoppingCart.setUserId(userId);
        shoppingCart.setNum(num);
        shoppingCart.setSkuId(skuId);
        shoppingCart.setId(null);
        shoppingCart.setCreateTime(null);
        shoppingCart.setUpdateTime(null);

        //5.写入数据库
        shoppingCartService.saveOrUpdate(shoppingCart);


    }


    /**
     * 分页查询购物车
     */
    @Override
    public PageResult<CartVO> pageList(CartPageDTO cartPageDTO) {

        //0.准备数据
        Long pageNo = cartPageDTO.getPageNo();
        Long pageSize = cartPageDTO.getPageSize();
        List<OrderItem> orderItems = OrderItemUtils.buildOrderItem(cartPageDTO.getOrderClazzList());

        //获取当前用户的id
        UserDTO userDTO = UserHolder.get();
        Long userId = userDTO.getId();

        //2.分页查询

        //构建分页条件
        Page<ShoppingCart> page = new Page<>(pageNo, pageSize);
        page.addOrder(orderItems);
        shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getUserId, userId)
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
            cartVO.setNewPrice(itemSku.getSpecialPrice());
            cartVO.setStock(itemSku.getStock());
            cartVO.setStatus(ItemStatus.ITEM_NORMAL);
            //判断商品状态
            // TODO 根据店铺id查询店铺状态
            //判断商品是否下架
            if (!itemSku.getOnSale()) {
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

        //判断数量是否超过最大值
        if (num > Constant.CART_MAX_NUM) {
            throw new CartNumExceedMaxException("数量超过最大值");
        }
        if (num <= 0) {
            throw new CartException("数量最少为1");
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
     * 进入商品详情页
     */
    @Override
    public ItemDetailVO detail(Long id) {

        //0.准备数据
        Long uid = UserHolder.get().getId();

        //查询购物车
        ShoppingCart cart = shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getId, id)
                .eq(ShoppingCart::getUserId, uid)
                .one();

        //判断购物车是否存在
        if (cart == null) {
            throw new CartDoesntExistException("购物车不存在");
        }

        Long baseId = cart.getBaseId();


        ItemBase itemBase = new ItemBase();
        ItemDetailVO itemDetailVO = new ItemDetailVO();
        //1.根据base_id查询商品基础信息
        //查询缓存
        ItemBaseVO itemBaseVO = new ItemBaseVO();
        String itemBaseJson = redisTemplate.opsForValue().get(Constant.ITEM_SPU_CACHE_KEY_PREFIX + baseId);
        //判断缓存是否为空
        if (itemBaseJson == null) {
            //查询数据库
            itemBase = itemBaseService.getById(baseId);
            //判断商品基础信息是否存在
            if (itemBase == null) {
                throw new ItemDoesntExistException("商品不存在");
            }
            //缓存商品基础信息
            redisTemplate.opsForValue().set(Constant.ITEM_SPU_CACHE_KEY_PREFIX + baseId, JSONUtil.toJsonStr(itemBase), TTLOffset.getRandomTTL(Constant.ITEM_SPU_CACHE_TTL), TimeUnit.MINUTES);
        } else {
            //转为ItemBaseVO
            itemBase = JSONUtil.toBean(itemBaseJson, ItemBase.class);
        }
        BeanUtil.copyProperties(itemBase, itemBaseVO);


        //2.根据spu_id查询商品sku信息
        //查询缓存
        List<ItemSku> itemSkuList = new ArrayList<>();
        List<String> skuIdJsonList = redisTemplate.opsForList().range(Constant.SPU_SKUS_CACHE_KEY_PREFIX + baseId, 0, -1);
        //判断缓存是否为空
        if (skuIdJsonList == null || skuIdJsonList.isEmpty()) {
            //查询数据库
            itemSkuList = itemSkuService.lambdaQuery()
                    .eq(ItemSku::getBaseId, baseId)
                    .eq(ItemSku::getOnSale, Boolean.TRUE)
                    .list();
            //判断商品sku列表是否为空
            if (itemSkuList == null || itemSkuList.isEmpty()) {
                throw new ItemSkuDoesntExistException("该sku不存在");
            }
            //缓存商品sku_ids
            itemService.setIdsToListCache(Constant.SPU_SKUS_CACHE_KEY_PREFIX + baseId, itemSkuList, ItemSku::getId, TTLOffset.getRandomTTL(Constant.SPU_SKUS_CACHE_TTL), TimeUnit.MINUTES);

            //批量缓存sku
            itemService.multiSetStringCache(itemSkuList, Constant.ITEM_SKU_CACHE_TTL, TimeUnit.MINUTES);
        } else {
            List<Long> ids = skuIdJsonList.stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            //保存不存在的sku_id
            List<Long> notExistIds = new ArrayList<>();
            //从缓存获取sku
            for (Long Id : ids) {
                String jsonObj = redisTemplate.opsForValue().get(Constant.ITEM_SKU_CACHE_KEY_PREFIX + Id);
                if (jsonObj == null) {
                    notExistIds.add(Id);
                } else {
                    itemSkuList.add(JSONUtil.toBean(jsonObj, ItemSku.class));
                }
            }
            List<ItemSku> list = null;
            if (!notExistIds.isEmpty()) {
                //查询缺失的sku
                list = itemSkuService.lambdaQuery()
                        .eq(ItemSku::getBaseId, baseId)
                        .eq(ItemSku::getOnSale, Boolean.TRUE)
                        .in(ItemSku::getId, notExistIds)
                        .list();
                //判断缺失的sku列表是否为空
                if (list == null || list.isEmpty()) {
                    throw new ItemSkuDoesntExistException("该sku不存在");
                }
                //缓存缺失sku
                itemService.multiSetStringCache(list, Constant.ITEM_SKU_CACHE_TTL, TimeUnit.MINUTES);
            }
            //合并sku列表
            if (list != null) {
                itemSkuList.addAll(list);
            }
        }


        //3.拼接返回
        BeanUtil.copyProperties(itemBaseVO, itemDetailVO);
        itemDetailVO.setSkuVOList(BeanUtil.copyToList(itemSkuList, ItemSkuVO.class));

        return itemDetailVO;


    }


    /**
     * 批量删除购物车
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void batchRemove(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {
            return;
        }

        //0.准备数据
        Long uid = UserHolder.get().getId();


        //1.删除购物车
        QueryWrapper<ShoppingCart> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ShoppingCart::getUserId, uid).in(ShoppingCart::getId, ids);
        shoppingCartService.remove(queryWrapper);


        //2.返回
    }
}
