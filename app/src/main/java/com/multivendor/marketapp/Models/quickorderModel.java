package com.multivendor.marketapp.Models;

import java.util.List;

public class quickorderModel {

    public class AddquickordResp {
        public String success;
        public quickordResult result;
        public String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public quickordResult getResult() {
            return result;
        }

        public void setResult(quickordResult result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class singlequickordResp {
        public String success;
        public quickordResult result;
        public String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public quickordResult getResult() {
            return result;
        }

        public void setResult(quickordResult result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class quickordResp {
        public String success;
        public List<quickordResult> result;
        public String message;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }

        public List<quickordResult> getResult() {
            return result;
        }

        public void setResult(List<quickordResult> result) {
            this.result = result;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }

    public class quickordResult {
        public String quick_id;
        public String quick_order_id;
        public String order_quote_status;
        public String subtotal;
        public String total_price;
        public String order_date;
        public String customer_name;
        public String customer_phone;
        public String customer_address;
        public String shipping_charge;
        public String payment_method;
        public String transaction_id;
        public String status;
        public String seller_status;
        public String user_status;
        public String invoice;
        public List<images> images;
        public String user_id;
        public String store_id;
        public String created_at;
        public String updated_at;
        public List<quick_products> products;
        public List<cartModel.orderModel> orderstatus;
        public String expected_delivery;
        public String cancel_reason;
        public String delivery_instructions;
        public String order_instructions;
        public String getDelivery_instructions() {
            return delivery_instructions;
        }

        public void setDelivery_instructions(String delivery_instructions) {
            this.delivery_instructions = delivery_instructions;
        }


        public String getOrder_instructions() {
            return order_instructions;
        }

        public void setOrder_instructions(String order_instructions) {
            this.order_instructions = order_instructions;
        }

        public String getCancel_reason() {
            return cancel_reason;
        }

        public void setCancel_reason(String cancel_reason) {
            this.cancel_reason = cancel_reason;
        }

        public String getExpected_delivery() {
            return expected_delivery;
        }

        public void setExpected_delivery(String expected_delivery) {
            this.expected_delivery = expected_delivery;
        }

        public String getQuick_id() {
            return quick_id;
        }

        public void setQuick_id(String quick_id) {
            this.quick_id = quick_id;
        }

        public String getQuick_order_id() {
            return quick_order_id;
        }

        public void setQuick_order_id(String quick_order_id) {
            this.quick_order_id = quick_order_id;
        }

        public String getOrder_quote_status() {
            return order_quote_status;
        }

        public void setOrder_quote_status(String order_quote_status) {
            this.order_quote_status = order_quote_status;
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

        public String getOrder_date() {
            return order_date;
        }

        public void setOrder_date(String order_date) {
            this.order_date = order_date;
        }

        public String getCustomer_name() {
            return customer_name;
        }

        public void setCustomer_name(String customer_name) {
            this.customer_name = customer_name;
        }

        public String getCustomer_phone() {
            return customer_phone;
        }

        public void setCustomer_phone(String customer_phone) {
            this.customer_phone = customer_phone;
        }

        public String getCustomer_address() {
            return customer_address;
        }

        public void setCustomer_address(String customer_address) {
            this.customer_address = customer_address;
        }

        public String getShipping_charge() {
            return shipping_charge;
        }

        public void setShipping_charge(String shipping_charge) {
            this.shipping_charge = shipping_charge;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSeller_status() {
            return seller_status;
        }

        public void setSeller_status(String seller_status) {
            this.seller_status = seller_status;
        }

        public String getUser_status() {
            return user_status;
        }

        public void setUser_status(String user_status) {
            this.user_status = user_status;
        }

        public String getInvoice() {
            return invoice;
        }

        public void setInvoice(String invoice) {
            this.invoice = invoice;
        }

        public List<quickorderModel.images> getImages() {
            return images;
        }

        public void setImages(List<quickorderModel.images> images) {
            this.images = images;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
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

        public List<quick_products> getProducts() {
            return products;
        }

        public void setProducts(List<quick_products> products) {
            this.products = products;
        }

        public List<cartModel.orderModel> getOrderstatus() {
            return orderstatus;
        }

        public void setOrderstatus(List<cartModel.orderModel> orderstatus) {
            this.orderstatus = orderstatus;
        }
    }

    public static class quick_products {
        public quick_products(String product_name, String qty, String price) {
            this.product_name = product_name;
            this.qty = qty;
            this.price = price;
        }

        public String quick_item_id;
        public String quick_id;
        public String product_name;
        public String qty;
        public String price;
        public String row_price;
        public String quick_status;
        public String user_id;
        public String store_id;
        public String created_at;
        public String updated_at;

        public String getQuick_item_id() {
            return quick_item_id;
        }

        public void setQuick_item_id(String quick_item_id) {
            this.quick_item_id = quick_item_id;
        }

        public String getQuick_id() {
            return quick_id;
        }

        public void setQuick_id(String quick_id) {
            this.quick_id = quick_id;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
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

        public String getQuick_status() {
            return quick_status;
        }

        public void setQuick_status(String quick_status) {
            this.quick_status = quick_status;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getStore_id() {
            return store_id;
        }

        public void setStore_id(String store_id) {
            this.store_id = store_id;
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

    public static class images {

        public String id;
        public String quick_id;
        public String image;
        public String created_at;
        public String updated_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getQuick_id() {
            return quick_id;
        }

        public void setQuick_id(String quick_id) {
            this.quick_id = quick_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
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
}
