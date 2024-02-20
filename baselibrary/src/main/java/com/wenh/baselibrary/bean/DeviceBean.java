package com.wenh.baselibrary.bean;

//设备信息
public class DeviceBean {

    //设备码
    private String code;
    //创建时间
    private String createTime;
    //设备的IP，当设置该值时，表示终端可以用设置该IP查询服务器存储的终端信息
    private String deviceIp;
    //主键
    private String deviceKey;
    //    设备名字
    private String deviceName;
    //    设备所在会议中心
    private String edificeKey;
    //    设备额外信息
    private String extra;
    //    设备类型
    private int identity;
    //    设备是否已经注册
    private boolean license;
    //    设备的LOGO
    private String logo;
    //    logo大小
    private String logoResolution;
    //    旧版本为mac地址，S07开始用uuid代替，uuid=md5(deviceId)
    private String mac;
    //    是否在线
    private boolean online;
    //    备注
    private String remark;
    //    设备分辨率
    private String resolution;
    //    设备所在的房间
    private String roomKey;
    //    会议室名字
    private String roomName;
    //    RTSP路径
    private String rtspPath;
    //    设备访问服务器接口时必须携带的秘钥
    private String secret;
    //    资源排列顺序
    private int sort;
    //    设备单元号
    private String unitId;
    //    上次修改时间
    private String updateTime;
    //    设备唯一标识
    private String uuid;
    //    该字段当前修改版本
    private int version;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }

    public String getDeviceKey() {
        return deviceKey;
    }

    public void setDeviceKey(String deviceKey) {
        this.deviceKey = deviceKey;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getEdificeKey() {
        return edificeKey;
    }

    public void setEdificeKey(String edificeKey) {
        this.edificeKey = edificeKey;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public int getIdentity() {
        return identity;
    }

    public void setIdentity(int identity) {
        this.identity = identity;
    }

    public boolean isLicense() {
        return license;
    }

    public void setLicense(boolean license) {
        this.license = license;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLogoResolution() {
        return logoResolution;
    }

    public void setLogoResolution(String logoResolution) {
        this.logoResolution = logoResolution;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }

    public String getRoomKey() {
        return roomKey;
    }

    public void setRoomKey(String roomKey) {
        this.roomKey = roomKey;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getRtspPath() {
        return rtspPath;
    }

    public void setRtspPath(String rtspPath) {
        this.rtspPath = rtspPath;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUuid() {
        return uuid;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "code='" + code + '\'' +
                ", createTime='" + createTime + '\'' +
                ", deviceIp='" + deviceIp + '\'' +
                ", deviceKey='" + deviceKey + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", edificeKey='" + edificeKey + '\'' +
                ", extra='" + extra + '\'' +
                ", identity=" + identity +
                ", license=" + license +
                ", logo='" + logo + '\'' +
                ", logoResolution='" + logoResolution + '\'' +
                ", mac='" + mac + '\'' +
                ", online=" + online +
                ", remark='" + remark + '\'' +
                ", resolution='" + resolution + '\'' +
                ", roomKey='" + roomKey + '\'' +
                ", roomName='" + roomName + '\'' +
                ", rtspPath='" + rtspPath + '\'' +
                ", secret='" + secret + '\'' +
                ", sort=" + sort +
                ", unitId='" + unitId + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", uuid='" + uuid + '\'' +
                ", version=" + version +
                '}';
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
