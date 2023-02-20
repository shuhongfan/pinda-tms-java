package com.itheima.pinda.vo.work;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("位置打点")
public class PointDTO {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("坐标")
    private MarkerPoint markerPoint;

    public void setMarkerPoints(String lng, String lat) {
        MarkerPoint markerPoint = new MarkerPoint();
        markerPoint.setLat(lat);
        markerPoint.setLng(lng);
        this.markerPoint = markerPoint;
    }

    @Data
    class MarkerPoint {
        @ApiModelProperty("精度")
        private String lng;
        @ApiModelProperty("纬度")
        private String lat;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PointDTO) {
            PointDTO orderPointDTO = (PointDTO) obj;
            return (name.equals(orderPointDTO.name));
        }
        return super.equals(obj);
    }
}
