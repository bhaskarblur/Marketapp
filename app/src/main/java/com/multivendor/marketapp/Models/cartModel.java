package com.multivendor.marketapp.Models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class cartModel {

    public class singlecartResp {
        private String success;
        private singlecartResult result;
        private String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public singlecartResult getResult() {
            return result;
        }

        public void setResult(singlecartResult result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class multcartResp {
        private String success;
        private List<singlecartResult> result;
        private String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public List<singlecartResult> getResult() {
            return result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class cartResp {
        private String success;
        private cartResult result;
        private String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public cartResult getResult() {
            return result;
        }

        public void setResult(cartResult result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class cartResult {
        private String order_id;
        private String order_increment_id;
        private String order_quote_status;
        private String user_id;
        private String subtotal;
        private String total_price;
        private String shipping_charge;
        private String cod_charge;
        private String status;
        private String invoice;
        private String payment_method;
        private String transaction_id;
        private String order_date;
        private String store;
        private String customer_name;
        private String customer_number;
        private String customer_address;
        private String delivery_instruction;

        public String getDelivery_instruction() {
            return delivery_instruction;
        }

        public void setDelivery_instruction(String delivery_instruction) {
            this.delivery_instruction = delivery_instruction;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getCustomer_number() {
            return customer_number;
        }

        public void setCustomer_number(String customer_number) {
            this.customer_number = customer_number;
        }

        public String getCustomer_address() {
            return customer_address;
        }

        public void setCustomer_address(String customer_address) {
            this.customer_address = customer_address;
        }
        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        private String created_at;
        private String updated_at;
        private List<productResult> products;
        private String cart_session;
        private String cart_id;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_increment_id() {
            return order_increment_id;
        }

        public void setOrder_increment_id(String order_increment_id) {
            this.order_increment_id = order_increment_id;
        }

        public String getOrder_quote_status() {
            return order_quote_status;
        }

        public void setOrder_quote_status(String order_quote_status) {
            this.order_quote_status = order_quote_status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getShipping_charge() {
            return shipping_charge;
        }

        public void setShipping_charge(String shipping_charge) {
            this.shipping_charge = shipping_charge;
        }

        public String getCod_charge() {
            return cod_charge;
        }

        public void setCod_charge(String cod_charge) {
            this.cod_charge = cod_charge;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public List<productResult> getProducts() {
            return products;
        }

        public void setProducts(List<productResult> products) {
            this.products = products;
        }

        public String getCart_session() {
            return cart_session;
        }

        public void setCart_session(String cart_session) {
            this.cart_session = cart_session;
        }

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }
    }

    public class singlecartResult {

        private String order_id;
        private String order_increment_id;
        private String order_quote_status;
        private String user_id;
        private String subtotal;
        private String total_price;
        private String shipping_charge;
        private String cod_charge;
        private String status;
        public String cancel_reason;

        public String getCancel_reason() {
            return cancel_reason;
        }

        public void setCancel_reason(String cancel_reason) {
            this.cancel_reason = cancel_reason;
        }
        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        private List<orderModel> orderstatus;
        private String invoice;
        private String payment_method;
        private String transaction_id;
        private String order_date;
        private String store;
        private String customer_name;
        @SerializedName("customer_phone")
        private String customer_number;
        private String customer_address;

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getCustomer_number() {
            return customer_number;
        }

        public void setCustomer_number(String customer_number) {
            this.customer_number = customer_number;
        }

        public String getCustomer_address() {
            return customer_address;
        }

        public void setCustomer_address(String customer_address) {
            this.customer_address = customer_address;
        }
        public String getStore() {
            return store;
        }

        public void setStore(String store) {
            this.store = store;
        }

        private String created_at;
        private String updated_at;
        private List<productResult> products;
        private String cart_session;
        private String cart_id;

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getOrder_increment_id() {
            return order_increment_id;
        }

        public void setOrder_increment_id(String order_increment_id) {
            this.order_increment_id = order_increment_id;
        }

        public String getOrder_quote_status() {
            return order_quote_status;
        }

        public void setOrder_quote_status(String order_quote_status) {
            this.order_quote_status = order_quote_status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getSubtotal() {
            return subtotal;
        }

        public void setSubtotal(String subtotal) {
            this.subtotal = subtotal;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getShipping_charge() {
            return shipping_charge;
        }

        public void setShipping_charge(String shipping_charge) {
            this.shipping_charge = shipping_charge;
        }

        public String getCod_charge() {
            return cod_charge;
        }

        public void setCod_charge(String cod_charge) {
            this.cod_charge = cod_charge;
        }

        public List<orderModel> getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(List<orderModel> orderstatus) {
            this.orderstatus = orderstatus;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public String getPayment_method() {
            return payment_method;
        }

        public void setPayment_method(String payment_method) {
            this.payment_method = payment_method;
        }

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public List<productResult> getProducts() {
            return products;
        }

        public void setProducts(List<productResult> products) {
            this.products = products;
        }

        public String getCart_session() {
            return cart_session;
        }

        public void setCart_session(String cart_session) {
            this.cart_session = cart_session;
        }

        public String getCart_id() {
            return cart_id;
        }

        public void setCart_id(String cart_id) {
            this.cart_id = cart_id;
        }
    }

    public class productResult {

        private String item_id;
        private String order_id;
        private String product_id;
        private String user_id;
        private String variant_id;
        private String sku;
        private String product_name;
        private String size;
        private String qty;
        private String price;
        private String row_price;
        private String item_status;
        private String created_at;
        private String updated_at;

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getVariant_id() {
            return variant_id;
        }

        public void setVariant_id(String variant_id) {
            this.variant_id = variant_id;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getRow_price() {
            return row_price;
        }

        public void setRow_price(String row_price) {
            this.row_price = row_price;
        }

        public String getItem_status() {
            return item_status;
        }

        public void setItem_status(String item_status) {
            this.item_status = item_status;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }

    public static class cartqtyandsize {

        public String product_id;
        public String product_name;
        public String size_id;
        public String size_name;
        public String qty;

        public cartqtyandsize() {
        }

        public cartqtyandsize(String product_id, String product_name, String size_id, String size_name, String qty) {
            this.product_id = product_id;
            this.product_name = product_name;
            this.size_id = size_id;
            this.size_name = size_name;
            this.qty = qty;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public String getSize_id() {
            return size_id;
        }

        public void setSize_id(String size_id) {
            this.size_id = size_id;
        }

        public String getSize_name() {
            return size_name;
        }

        public void setSize_name(String size_name) {
            this.size_name = size_name;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }
    }

    public static class orderModel{
        private String id;
        private String order_id;
        private String item_id;
        private String store_id;
        private String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrder_id() {
            return order_id;
        }

        public void setOrder_id(String order_id) {
            this.order_id = order_id;
        }

        public String getItem_id() {
            return item_id;
        }

        public void setItem_id(String item_id) {
            this.item_id = item_id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
