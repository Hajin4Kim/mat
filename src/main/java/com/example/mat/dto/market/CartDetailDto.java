package com.example.mat.dto.market;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CartDetailDto {

    // 장바구니 조회 페이지로 전달
    // 장바구니에 담겨있는 상품 조회

    private Long cartitemid; 

    private String itemName;

    private String price;

    private int quantity;
    
   

    


    
}