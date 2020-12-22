package itpainter.service;

import itpainter.exception.ClientException;
import itpainter.mapper.UserMapper;
import itpainter.model.Blog;
import itpainter.model.PageBean;
import itpainter.model.PageSearch;
import itpainter.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class UserService {

    private static final String SALT = "itpainter";

//    @Value("${user.head.local-path}")
//    private String localPath;
//    @Value("${user.head.remote-path}")
//    private String remotePath;
//    @Value("${user.head.filename}")
//    private String fileName;

    @Autowired
     UserMapper userMapper;

    public User login(User user) {
        if (user.getUsername()==null||user.getUsername().length()==0||user.getPassword()==null||user.getPassword().length()==0)
            throw new ClientException("USR000","信息输入不完整");
        User query = new User();
        query.setUsername(user.getUsername());

        User exist = userMapper.selectOne(query);//只根据用户名查询用户信息
        user.setPassword(md5(user.getPassword()));
        if (exist == null)
            throw new ClientException("USR001", "用户不存在");
        if (!exist.getPassword().equals(user.getPassword()))
            throw new ClientException("USR002", "用户名密码校验失败");
        return exist;
    }

    private String md5(String password) {
        return SALT.charAt(5) + DigestUtils.md5DigestAsHex(password.getBytes()) + SALT.substring(2, 5);
    }

//    //默认事务的配置为Spring事务的传播特性=required，事务隔离级别为数据库的默认事务隔离级别
//    @Transactional
//    public void register(User user, MultipartFile headFile) {
//
//        //根据用户名查询已有的用户信息，如果存在，不允许注册
//        User query = new User();
//        query.setUsername(user.getUsername());
//        User exist = userMapper.selectOne(query);
//        if(exist != null)
//            throw new ClientException("USR003", "用户已存在");
//
//        //保存注册用户信息（插入）
//        String path = "/"+user.getUsername()+"/"+fileName;
//        user.setHead(remotePath+path);
//        userMapper.insertSelective(user);//插入成功之后，自增主键通过mybatis的<selectKey返回对象
//
//        //注册用户时，完成初始化设置：创建setting数据
//        Setting setting = new Setting();
//        setting.setUserId(user.getId());
//        setting.setBatchNumber(8);
//        settingService.add(setting);
//
//        //保存注册用户头像到本地文件夹
//        FileOutputStream fos = null;
//        BufferedOutputStream bos = null;
//        try {
//            try {
//                File dir = new File(localPath+"/"+user.getUsername());
//                dir.mkdirs();
//                fos = new FileOutputStream(localPath+path);
//                bos = new BufferedOutputStream(fos);
//                bos.write(headFile.getBytes());
//                bos.flush();
//            } finally {
//                if(bos != null)
//                    bos.close();
//                if(fos != null)
//                    fos.close();
//            }
//        } catch (IOException e) {
//            //打印捕获的异常，抛出自定义异常，统一异常拦截器进行打印自定义异常
////            e.printStackTrace();
////            throw new SystemException("USR004", "用户注册失败：头像上传出错");
//            throw new SystemException("USR004", "用户注册失败：头像上传出错", e);
//        }
//    }
}
