package com.example.gccoffeejiwon.repository;

import com.example.gccoffeejiwon.model.*;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static com.example.gccoffeejiwon.JdbcUtils.toLocalTimeStamp;
import static com.example.gccoffeejiwon.JdbcUtils.toUUID;

@Repository
public class OrderJdbcRepository implements OrderRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public OrderJdbcRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @Transactional
    public Order insert(Order order) {
        jdbcTemplate.update("INSERT INTO orders(order_id, email, address, postcode, order_status, created_at, updated_at) " +
                        "VALUES (UUID_TO_BIN(:orderId), :email, :address, :postcode, :orderStatus, :createdAt, :updatedAt)",
                toOrderParamMap(order));
        order.getOrderItems()
                .forEach(item ->
                        jdbcTemplate.update("INSERT INTO order_items(order_id, product_id, category, price, quantity, created_at, updated_at) " +
                                        "VALUES (UUID_TO_BIN(:orderId), UUID_TO_BIN(:productId), :category, :price, :quantity, :createdAt, :updatedAt)",
                                toOrderItemParamMap(order.getOrderId(), order.getCreatedAt(), order.getUpdatedAt(), item)));
        return order;
    }

    @Override
    public List<OrderVO> findAll() {
        return jdbcTemplate.query("select * from orders", orderRowMapper);
    }

    @Override
    public Optional<OrderVO> findById(UUID orderId) {
        try {
            return Optional.of(
                    jdbcTemplate.queryForObject("SELECT * FROM orders WHERE order_id = UUID_TO_BIN(:orderId)",
                            Collections.singletonMap("orderId", orderId.toString().getBytes()), orderRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<OrderVO> findByEmail(Email email) {
        try {
            return Optional.of(
                    jdbcTemplate.queryForObject("SELECT * FROM orders WHERE email = :email",
                            Collections.singletonMap("email", email), orderRowMapper)
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<OrderItemVO> findAllItems(UUID orderId) {

            return List.of(
                    jdbcTemplate.queryForObject("SELECT * FROM order_items WHERE order_id = UUID_TO_BIN(:orderId)",
                            Collections.singletonMap("orderId", orderId.toString().getBytes()), orderItemRowMapper)
            );

    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE FROM orders", Collections.emptyMap());
    }

    @Override
    public void deleteOrder(UUID orderId) {
        jdbcTemplate.update("DELETE FROM orders where order_id = UUID_TO_BIN(:orderId)", Collections.singletonMap("orderId", orderId.toString().getBytes()));
    }

    private static final RowMapper<OrderVO> orderRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var email = new Email(resultSet.getString("email"));
        var address = resultSet.getString("address");
        var postcode = resultSet.getString("postcode");
        var orderStatus = OrderStatus.valueOf(resultSet.getString("order_status"));
        var createdAt = toLocalTimeStamp(resultSet.getTimestamp("created_at"));
        var updatedAt = toLocalTimeStamp(resultSet.getTimestamp("updated_at"));
        return new OrderVO(orderId, email, address, postcode, orderStatus, createdAt, updatedAt);
    };

    private static final RowMapper<OrderItemVO> orderItemRowMapper = (resultSet, i) -> {
        var orderId = toUUID(resultSet.getBytes("order_id"));
        var productId = toUUID(resultSet.getBytes("product_id"));
        var category = Category.valueOf(resultSet.getString("category"));
        var price = resultSet.getLong("price");
        var quantity = resultSet.getInt("quantity");
        return new OrderItemVO(orderId, productId, category, price, quantity);
    };

    private Map<String, Object> toOrderParamMap(Order order) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", order.getOrderId().toString().getBytes());
        paramMap.put("email", order.getEmail().getAddress());
        paramMap.put("address", order.getAddress());
        paramMap.put("postcode", order.getPostcode());
        paramMap.put("orderStatus", order.getOrderStatus().toString());
        paramMap.put("createdAt", order.getCreatedAt());
        paramMap.put("updatedAt", order.getUpdatedAt());
        return paramMap;
    }

    private Map<String, Object> toOrderItemParamMap(UUID orderId, LocalDateTime createdAt, LocalDateTime updatedAt, OrderItem item) {
        var paramMap = new HashMap<String, Object>();
        paramMap.put("orderId", orderId.toString().getBytes());
        paramMap.put("productId", item.productId().toString().getBytes());
        paramMap.put("category", item.category().toString());
        paramMap.put("price", item.price());
        paramMap.put("quantity", item.quantity());
        paramMap.put("createdAt", createdAt);
        paramMap.put("updatedAt", updatedAt);
        return paramMap;
    }
}
