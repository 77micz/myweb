package com.itcast.myweb.service.impl.client;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.itcast.myweb.common.Constant;
import com.itcast.myweb.common.exception.*;
import com.itcast.myweb.common.pojo.PageResult;
import com.itcast.myweb.domain.dto.OrderDTO;
import com.itcast.myweb.domain.dto.OrderPageDTO;
import com.itcast.myweb.domain.entity.*;
import com.itcast.myweb.domain.vo.OrderDetailVO;
import com.itcast.myweb.domain.vo.OrderVO;
import com.itcast.myweb.enums.MyOrderStatus;
import com.itcast.myweb.enums.OrderStatus;
import com.itcast.myweb.enums.PaymentType;
import com.itcast.myweb.mapper.OrderDetailMapper;
import com.itcast.myweb.service.client.ItemService;
import com.itcast.myweb.service.client.PayService;
import com.itcast.myweb.service.client.TradeService;
import com.itcast.myweb.service.common.*;
import com.itcast.myweb.utils.OrderItemUtils;
import com.itcast.myweb.utils.UniqueID;
import com.itcast.myweb.utils.UserHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;


/**
 * 交易服务实现类
 */
@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {


    //唯一id生成器
    private final UniqueID uniqueID;

    //订单服务
    private final IOrderService orderService;

    //地址服务
    private final IAddressService addressService;

    //购物车服务
    private final IShoppingCartService shoppingCartService;

    //sku服务
    private final IItemSkuService itemSkuService;

    //spu服务
    private final IItemBaseService itemBaseService;

    //订单明细服务
    private final IOrderDetailService orderDetailService;

    //订单详情mapper
    private final OrderDetailMapper orderDetailMapper;

    //商品服务
    private final ItemService itemService;

    //rabbitTemplate
    private final RabbitTemplate rabbitTemplate;

    //payOrderService
    private final IPayOrderService payOrderService;


    /**
     * 创建订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<Long> createOrder(OrderDTO orderDTO) {

        //0.准备数据

        //获取关联购物车id与收货地址id对应关系
        Map<Long, Long> cartIdAddressIdMap = orderDTO.getCartIdAddressIdMap();
        //获取当前用户id
        Long userId = UserHolder.get().getId();
        //获取支付方式
        PaymentType paymentType = orderDTO.getPaymentType();
        //获取sku_id
        Long skuId = orderDTO.getSkuId();
        //获取数量
        Integer num = orderDTO.getNum();
        //获取收货地址id
        Long addressId = orderDTO.getAddressId();


        List<Long> orderIds = new ArrayList<>();
        //判断在哪创建订单
        if (cartIdAddressIdMap != null && !cartIdAddressIdMap.isEmpty()) {
            //获取购物车ids
            List<Long> cartIds = new ArrayList<>(cartIdAddressIdMap.keySet());
            //获取收货地址ids
            List<Long> addressIds = new ArrayList<>(cartIdAddressIdMap.values());
            //判断收货地址是否存在
            if (addressIds.isEmpty()) {
                throw new AddressNotFoundException("收货地址不存在");
            }
            List<Address> addresses = addressService.lambdaQuery()
                    .in(Address::getId, addressIds)
                    .eq(Address::getUserId, userId)
                    .list();
            if (addresses == null || addresses.size() != addressIds.size()) {
                throw new AddressNotFoundException("收货地址缺失");
            }
            orderIds = cartMethod(userId, cartIds, paymentType, addresses, cartIdAddressIdMap);
        } else if (skuId != null) {
            //判断数量是否大于0
            if (num <= 0) {
                throw new OrderItemNumberException("数量必须大于0");
            }
            //创建订单
            orderIds = itemMethod(skuId, num, userId, paymentType, addressId);
        }


        return orderIds;


    }


    /**
     * 删除订单
     *
     * @param id 订单ID
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void removeById(Long id) {

        //0.获取用户数据
        Long userId = UserHolder.get().getId();

        //判断订单是否存在
        Long count = orderService.lambdaQuery()
                .eq(Order::getUserId, userId)
                .eq(Order::getId, id)
                .count();
        if (count == 0) {
            throw new OrderNotFoundException("订单不存在");
        }


        //1.删除订单
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);
        queryWrapper.eq("user_id", userId);

        orderService.remove(queryWrapper);


        //2.删除订单明细

        //判断明细是否存在
        Long detailCount = orderDetailService.lambdaQuery()
                .eq(OrderDetail::getOrderId, id)
                .count();
        if (detailCount == 0) {
            throw new OrderDetailMissException("订单明细不存在");
        }

        QueryWrapper<OrderDetail> orderDetailQueryWrapper = new QueryWrapper<>();
        orderDetailQueryWrapper.eq("order_id", id);
        orderDetailService.remove(orderDetailQueryWrapper);


    }


    /**
     * 订单详情
     */
    @Override
    public OrderDetailVO detail(Long id) {


        //0.准备数据
        Long userId = UserHolder.get().getId();


        //1.查询订单
        Order order = orderService.lambdaQuery()
                .eq(Order::getUserId, userId)
                .eq(Order::getId, id)
                .one();
        //判断订单是否存在
        if (order == null) {
            throw new OrderNotFoundException("订单不存在");
        }


        //2.查询订单明细
        OrderDetail orderDetail = orderDetailService.lambdaQuery()
                .eq(OrderDetail::getUserId, userId)
                .eq(OrderDetail::getOrderId, id)
                .one();
        //判断订单明细是否存在
        if (orderDetail == null) {
            throw new OrderDetailMissException("订单明细不存在");
        }


        //4.组装VO并返回
        return getOrderDetailVO(order, orderDetail);


    }


    /**
     * 取消订单
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void cancel(Long id) {

        //0.准备数据
        Long userId = UserHolder.get().getId();


        //1.查询订单
        Order order = orderService.lambdaQuery()
                .eq(Order::getUserId, userId)
                .eq(Order::getId, id)
                .one();

        //判断订单是否存在
        if (order == null) {
            throw new OrderNotFoundException("订单不存在");
        }
        //判断订单状态
        if (!order.getStatus().equals(OrderStatus.PENDING_PAY)) {
            throw new OrderStatusException("订单状态异常");
        }


        //查询订单明细
        OrderDetail orderDetail = orderDetailService.lambdaQuery()
                .eq(OrderDetail::getOrderId, id)
                .one();

        //判断订单明细是否存在
        if (orderDetail == null) {
            throw new OrderDetailMissException("订单明细不存在");
        }

        Long skuId = orderDetail.getSkuId();
        Integer num = orderDetail.getNum();


        //2.修改订单
        LocalDateTime now = LocalDateTime.now();
        orderService.lambdaUpdate()
                .eq(Order::getId, id)
                .eq(Order::getUserId, userId)
                .eq(Order::getStatus,OrderStatus.PENDING_PAY)
                .set(Order::getStatus, OrderStatus.CANCEL)
                .set(Order::getUpdateTime, now)
                .set(Order::getCancelTime, now)
                .set(Order::getCompleteTime, now)
                .update();


        //3.归还库存
        itemSkuService.lambdaUpdate()
                .eq(ItemSku::getId, skuId)
                .setSql(num > 0, "stock = stock + " + num)
                .update();


    }


    /**
     * 确认收货
     */
    @Override
    public void confirm(Long id) {
        //0.准备数据
        Long userId = UserHolder.get().getId();


        //1.查询订单
        Order order = orderService.lambdaQuery()
                .eq(Order::getUserId, userId)
                .eq(Order::getId, id)
                .one();
        //判断订单是否存在
        if (order == null) {
            throw new OrderNotFoundException("订单不存在");
        }
        //判断订单状态
        if (!order.getStatus().equals(OrderStatus.PENDING_RECEIVE)) {
            throw new OrderStatusException("订单状态异常");
        }


        //2.修改订单
        LocalDateTime now = LocalDateTime.now();
        orderService.lambdaUpdate()
                .eq(Order::getId, id)
                .eq(Order::getUserId, userId)
                .set(Order::getStatus, OrderStatus.COMPLETE)
                .set(Order::getUpdateTime, now)
                .set(Order::getCompleteTime, now)
                .update();
    }


    /**
     * 条件分页查询
     */
    @Override
    public PageResult<OrderVO> search(OrderPageDTO orderPageDTO) {

        //0.准备数据
        Long userId = UserHolder.get().getId();
        MyOrderStatus status = orderPageDTO.getStatus();
        Integer statusCode = status.getStatus();


        //1.分页查询订单详情
        //构造分页条件
        Page<OrderDetail> page = Page.of(orderPageDTO.getPageNo(), orderPageDTO.getPageSize());
        page.addOrder(OrderItemUtils.buildOrderItem(orderPageDTO.getOrderClazzList()));
        orderDetailMapper.selectPage(page, orderPageDTO, userId, statusCode);


        List<OrderDetail> orderDetailList = page.getRecords();

        return PageResult.<OrderVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(BeanUtil.copyToList(orderDetailList, OrderVO.class))
                .build();


    }


    //获取订单明细VO
    private OrderDetailVO getOrderDetailVO(Order order, OrderDetail orderDetail) {

        //1.查询支付单
        PayOrder payOrder = payOrderService.lambdaQuery()
                .eq(PayOrder::getBizOrderNo, order.getId())
                .one();
        if (payOrder == null) {
            throw new PayOrderMissException("支付单缺失");
        }

        OrderDetailVO orderDetailVO = new OrderDetailVO();
        BeanUtil.copyProperties(orderDetail, orderDetailVO);
        BeanUtil.copyProperties(order, orderDetailVO);
        orderDetailVO.setOrderDetailId(orderDetail.getId());
        orderDetailVO.setId(order.getId());
        //设置支付单号
        orderDetailVO.setPayOrderNo(payOrder.getPayOrderNo());
        return orderDetailVO;
    }


    //商品页面创建订单
    private List<Long> itemMethod(Long skuId, Integer num, Long userId, PaymentType paymentType, Long addressId) {
        //1.查询商品
        ItemSku itemSku = itemSkuService.lambdaQuery()
                .eq(ItemSku::getId, skuId)
                .one();

        //判断库存是否足够
        if (itemSku.getStock() < num) {
            throw new ItemStockInsufficientException("库存不足");
        }
        //库存充足，扣减库存
        itemService.deduckStock(itemSku.getId(), num);

        //关联skuId与地址
        Map<Long, Address> skuIdAddressMap = new HashMap<>();
        Address address = addressService.lambdaQuery()
                .eq(Address::getId, addressId)
                .eq(Address::getUserId, userId)
                .one();
        //判断地址是否存在
        if (address == null) {
            throw new AddressMissException("收货地址缺失");
        }
        skuIdAddressMap.put(itemSku.getId(), address);


        //2.创建订单与订单明细

        return saveOrder(Collections.singletonList(itemSku), Collections.singletonMap(itemSku.getId(), num), userId, paymentType, skuIdAddressMap);
    }


    //购物车创建订单
    private List<Long> cartMethod(Long userId, List<Long> cartIds, PaymentType paymentType, List<Address> addresses, Map<Long, Long> cartIdAddressIdMap) {

        //判断关联购物车id与收货地址id对应关系是否正确
        if (cartIdAddressIdMap == null || cartIdAddressIdMap.isEmpty()) {
            throw new CartItemMissException("购物车id与收货地址id对应关系不能为空");
        }

        //1.查询购物车
        List<ShoppingCart> cartList = shoppingCartService.lambdaQuery()
                .eq(ShoppingCart::getUserId, userId)
                .in(ShoppingCart::getId, cartIds)
                .list();

        //判断购物车是否存在
        if (cartList == null || cartList.size() != cartIds.size()) {
            throw new CartItemMissException("购物车商品缺失");
        }

        //关联cartId与skuId
        Map<Long, Long> cartIdSkuIdMap = cartList.stream().collect(Collectors.toMap(ShoppingCart::getId, ShoppingCart::getSkuId));

        //循环处理购物车id，获取skuId与地址关联
        Map<Long, Address> skuIdAddressMap = new HashMap<>();
        for (Long cartId : cartIds) {
            Long skuId = cartIdSkuIdMap.get(cartId);
            Long addressId = cartIdAddressIdMap.get(cartId);
            Address address = addresses.stream().filter(addr -> addr.getId().equals(addressId)).findFirst().orElse(null);
            if (address == null) {
                throw new AddressNotFoundException("收货地址缺失");
            }
            skuIdAddressMap.put(skuId, address);
        }


        //2.查询商品
        List<ItemSku> itemSkus = itemSkuService.lambdaQuery()
                .in(ItemSku::getId, cartList.stream().map(ShoppingCart::getSkuId).collect(Collectors.toList()))
                .list();

        //判断商品是否缺失
        if (itemSkus == null || itemSkus.size() != cartList.size()) {
            throw new CartItemMissException("购物车商品缺失");
        }

        //关联skuId和购物车
        Map<Long, ShoppingCart> skuIdCartMap = cartList.stream().collect(Collectors.toMap(ShoppingCart::getSkuId, cart -> cart));

        //skuId与num关联
        Map<Long, Integer> skuIdNumMap = cartList.stream().collect(Collectors.toMap(ShoppingCart::getSkuId, ShoppingCart::getNum));

        //遍历商品判断库存是否足够
        for (ItemSku itemSku : itemSkus) {
            if (itemSku.getStock() < skuIdCartMap.get(itemSku.getId()).getNum()) {
                throw new ItemStockInsufficientException("库存不足");
            }
            //库存充足，扣减库存
            itemService.deduckStock(itemSku.getId(), skuIdCartMap.get(itemSku.getId()).getNum());
        }


        //2.创建订单与订单明细
        List<Long> ids = saveOrder(itemSkus, skuIdNumMap, userId, paymentType, skuIdAddressMap);


        //4.删除购物车
        shoppingCartService.removeByIds(cartIds);

        return ids;
    }


    //订单写入数据库
    private List<Long> saveOrder(List<ItemSku> itemSkuList, Map<Long, Integer> numMap, Long userId, PaymentType paymentType, Map<Long, Address> skuIdAddressMap) {


        //查询spu折扣
        List<ItemBase> spuList = itemBaseService.lambdaQuery()
                .in(ItemBase::getId, itemSkuList.stream().map(ItemSku::getBaseId).collect(Collectors.toList()))
                .list();
        //关联id与spu
        Map<Long, ItemBase> spuMap = spuList.stream().collect(Collectors.toMap(ItemBase::getId, itemBase -> itemBase));


        List<OrderDetail> orderDetailList = new ArrayList<>();
        List<Order> orderList = new ArrayList<>();

        List<Long> ids = new ArrayList<>();//订单id列表
        //遍历构建订单与明细
        for (ItemSku itemSku : itemSkuList) {

            //订单id
            Long uniqueId = uniqueID.getUniqueId(Constant.ORDER_UNIQUE_ID_CACHE_KEY_PREFIX);
            ids.add(uniqueId);

            orderDetailList.add(getOrderDetail(itemSku, uniqueId, numMap.get(itemSku.getId()), userId, spuMap.get(itemSku.getBaseId())));

            orderList.add(getOrder(userId, paymentType, skuIdAddressMap.get(itemSku.getId()), uniqueId));

        }

        //批量插入订单与明细

        if (!orderDetailList.isEmpty()) {
            orderDetailService.saveBatch(orderDetailList);
        }
        if (!orderList.isEmpty()) {
            orderService.saveBatch(orderList);
        }

        return ids;


    }


    //获取订单
    private static Order getOrder(Long userId, PaymentType paymentType, Address address, Long uniqueId) {
        Order order = new Order();
        order.setId(uniqueId);
        order.setUserId(userId);
        order.setPaymentType(paymentType);
        order.setAddressId(address.getId());
        order.setRecipient(address.getReceiverName());
        order.setPhone(address.getReceiverPhone());
        return order;
    }


    //获取订单明细
    private OrderDetail getOrderDetail(ItemSku itemSku, Long uniqueId, Integer num, Long userId, ItemBase spu) {

        OrderDetail orderDetail = new OrderDetail();
        BeanUtil.copyProperties(itemSku, orderDetail);
        orderDetail.setNum(num);
        orderDetail.setUserId(userId);
        orderDetail.setSkuId(itemSku.getId());
        orderDetail.setOrderId(uniqueId);

        //计算总价
        BigDecimal price = itemSku.getPrice();
        BigDecimal multiplicand = new BigDecimal(num);
        BigDecimal totalPrice = price.multiply(multiplicand);
        orderDetail.setTotalPrice(totalPrice);
        //计算优惠价
        BigDecimal discount = spu.getDiscount();
        if (discount != null) {
            BigDecimal discountPrice = totalPrice.multiply(discount);
            orderDetail.setPreferential(discountPrice);
        } else {
            orderDetail.setPreferential(BigDecimal.ZERO);
        }
        //暂时没有运费
        orderDetail.setShippingFee(BigDecimal.ZERO);
        //实付款设为优惠后价格
        orderDetail.setActualPayment(totalPrice.subtract(orderDetail.getPreferential()));


        orderDetail.setId(null);
        orderDetail.setCreateTime(null);
        orderDetail.setUpdateTime(null);
        orderDetail.setDeleted(null);


        return orderDetail;
    }
}
