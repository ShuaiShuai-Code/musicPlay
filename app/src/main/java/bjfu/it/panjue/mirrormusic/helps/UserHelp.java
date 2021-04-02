package bjfu.it.panjue.mirrormusic.helps;

/*
    1.用户登录
    (1)当用户登录时，利用SharedPreferences 保存用户登录标记(手机号)
    (2)利用全局单例类UserHelp保存登录用户信息
       a.用户登录之后
       b.用户打开应用程序，检测haredPreferences 中是否存在登录用户标记：
         如果存在则为UserHelp进行赋值，并且进入主页
         如果不存在，则进入登录页面
    2.用户退出
    (1)删除掉haredPreferences 保存的用户标记，退出到登录页面
 */
public class UserHelp {

    private static UserHelp instance;

    private UserHelp(){

    }

    public static UserHelp getInstance(){
        if(instance == null){
            synchronized ((UserHelp.class)){
                if(instance == null){
                    instance = new UserHelp();
                }
            }
        }
        return instance;
    }

    private String phone;

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }
}
