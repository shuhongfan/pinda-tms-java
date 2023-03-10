package com.itheima.pinda.DTO;
import com.itheima.pinda.entity.Order;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
public class OrderClassifyGroupDTO {
    private String id;
    // 分组维度 key
    private String key;

    public void setKey(String key) {
        String[] keys = key.split("#");
        for (int i = 0; i < keys.length; i++) {
            switch (i) {
                case 0:
                    this.startAgencyId = keys[i];
                    break;
                case 1:
                    this.endAgencyId = keys[i];
                    break;
                case 2:
                    this.currentAgencyId = keys[i];
                    break;
//                case 3:
//                    this.goodsType = keys[i];
//                    break;
                // 扩充部分
            }
        }
    }

    /**
     * 分组信息
     */
    private String startAgencyId;
    private String endAgencyId;
    private Integer orderType;
    private String goodsType;
    private String currentAgencyId;

    /**
     * 是否为新订单，如果起始机构和当前机构相同则为新订单，否则为中转订单
     * @return
     */
    public boolean isNew() {
        return this.startAgencyId.equals(this.currentAgencyId);
    }

    /**
     * 原订单集合
     */
    private List<Order> orders;

    public OrderClassifyGroupDTO(String id,String key, String startAgencyId, String endAgencyId, Integer orderType, String goodsType, String currentAgencyId, List<Order> orders) {
        this.id = id;
        this.key = key;
        this.startAgencyId = startAgencyId;
        this.endAgencyId = endAgencyId;
        this.goodsType = goodsType;
        this.orderType = orderType;
        this.orders = orders;
        this.currentAgencyId = currentAgencyId;
        setKey(this.key);
    }
}
