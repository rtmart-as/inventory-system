package com.ychs.web.sale_return.entity;

import lombok.Data;
import org.apache.ibatis.type.Alias;

@Data
@Alias("SaleReturnApplyParm")
public class ApplyParm {
     private String returnId;
     private String type;
     private String applyUser;
     private String applyDesc;
}
