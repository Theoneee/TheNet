package the.one.library;

//  ┏┓　　　┏┓
//┏┛┻━━━┛┻┓
//┃　　　　　　　┃
//┃　　　━　　　┃
//┃　┳┛　┗┳　┃
//┃　　　　　　　┃
//┃　　　┻　　　┃
//┃　　　　　　　┃
//┗━┓　　　┏━┛
//    ┃　　　┃                  神兽保佑
//    ┃　　　┃                  永无BUG！
//    ┃　　　┗━━━┓
//    ┃　　　　　　　┣┓
//    ┃　　　　　　　┏┛
//    ┗┓┓┏━┳┓┏┛
//      ┃┫┫　┃┫┫
//      ┗┻┛　┗┻┛

/**
 * @author The one
 * @date 2019/1/17 0017
 * @describe TODO
 * @email 625805189@qq.com
 * @remark
 */
public class Builder {

    private String code = "code";

    private String msg = "msg";

    private String data = "data";

    private String page = "page";

    public String getCode() {
        return code;
    }

    public Builder setCode(String code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public Builder setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public String getData() {
        return data;
    }

    public Builder setData(String data) {
        this.data = data;
        return this;
    }

    public String getPage() {
        return page;
    }

    public Builder setPage(String page) {
        this.page = page;
        return this;
    }
}
