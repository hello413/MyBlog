package itpainter.controller;

import itpainter.exception.ClientException;
import itpainter.model.*;
import itpainter.service.CommentService;
import itpainter.service.TagService;
import itpainter.service.TypeService;
import itpainter.service.BlogService;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.jni.FileInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoader;


import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

@RestController
@RequestMapping("/visitor")
public class VisitorController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @PostMapping("/blogAsList")
    public Object blogAsList(PageSearch pageSearch, HttpServletRequest req) {
        //search:查询标签；currentPageStr:当前页码；pageSizeStr：每页显示条数
        if (pageSearch.getSearch() == null || pageSearch.getSearch().equals("null")) pageSearch.setSearch("");
        System.out.println(pageSearch.toString());
        PageBean<Blog> page = blogService.blogAsList(pageSearch);
        return page;
    }

    @PostMapping("/typeShow")
    public Object typeShow() {
        List<Type> list = typeService.typeAsList();
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            list.get(i).setBlogs(blogService.findByTypeId(id));
        }
        return list;
    }

    @RequestMapping("/tagsShow")
    public Object tagsShow() {
        List<Tag> list = tagService.tagAsList();
        for (int i = 0; i < list.size(); i++) {
            int id = list.get(i).getId();
            list.get(i).setBlogs(blogService.findByTagId(id));
        }
        return list;
    }

    @RequestMapping("/TimeAsBlog")
    public Object TimeAsBlog() {
        Map<Integer, List<Blog>> page = blogService.TimeAsBlog();
        return page;
    }

    @RequestMapping("/Blog")
    public Object Blog(int blog_id) {
        Blog page = null;
        if (blog_id == 0) {
            throw new RuntimeException("没有指定阅读文章");
        } else {
            page = blogService.Blog(blog_id);
        }
        return page;
    }

    @RequestMapping("/comments")
    public Object comments(int blog_id) {
        Map<Comment, List<Comment>> comment = null;
        if (blog_id == 0) {
            throw new RuntimeException("没有指定阅读文章");
        } else {
            comment = commentService.comment(blog_id);
        }
        List<MyMap> list = new ArrayList<>();
        for (Comment c : comment.keySet()) {
            MyMap myMap = new MyMap();
            myMap.setKey(c);
            myMap.setValue(comment.get(c));
            list.add(myMap);
        }
        return list;
    }

    @RequestMapping("/getFile")
    public Object getFile(HttpServletRequest request) {
        //获取pathName的File对象
        String path = ClassUtils.getDefaultClassLoader().getResource("").getPath();
        File dirFile = new File(path + "/static/img");
        System.out.println(dirFile.getPath());
        Map<String, ArrayList<MyFile>> map = new TreeMap<>();
        //判断该文件或目录是否存在，不存在时在控制台输出提醒
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            throw new RuntimeException("资源目录不存在或者该目录是个文件");
        }

        //获取此目录下的所有文件名与目录名
        String[] fileList = dirFile.list();

        for (int i = 0; i < fileList.length; i++) {
            //遍历文件目录
            String string = fileList[i];

            //File("documentName","fileName")是File的另一个构造器
            File file = new File(dirFile.getPath(), string);

            String name = file.getName();

            //如果是一个目录，输出目录名后，进行递归
            if (file.isDirectory()) {
                //递归
//                getFile(file.getCanonicalPath());
            } else {
                String type = name.split("\\.")[1].toLowerCase();
                //如果是文件，则直接输出文件名
                if (type.equals("jpg") || type.equals("png") || type.equals("gif")) {
                    ArrayList<MyFile> list = map.getOrDefault("图片", new ArrayList<>());
                    list.add(new MyFile(name, "/blog/img/" + name));
                    map.put("图片", list);
                } else if (type.equals("mp4") || type.equals("avi")) {
                    ArrayList<MyFile> list = map.getOrDefault("视频", new ArrayList<>());
                    list.add(new MyFile(name, "/blog/img/" + name));
                    map.put("视频", list);
                } else {
                    ArrayList<MyFile> list = map.getOrDefault("文本、压缩包或者其他格式", new ArrayList<>());
                    list.add(new MyFile(name, "/blog/img/" + name));
                    map.put("文本、压缩包或者其他格式", list);
                }
//                System.out.println(name);
            }
        }
        return map;
    }

    @RequestMapping("/download")
    public Object download(HttpServletResponse response, HttpServletRequest request, String filename) throws IOException {
        String realPath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + "static/img/" + filename.substring(10);
        File file = new File(realPath);
        filename = file.getName();
        System.out.println(file.getName());
        //2.2用字节流关联
        FileInputStream fis = new FileInputStream(realPath);

        //3.设置response的响应头
        //3.1设置响应头类型：content-type
        ServletContext servletContext = request.getServletContext();
        String mimeType = servletContext.getMimeType(filename);//获取文件的mime类型
        response.setHeader("content-type",mimeType);
        //3.2设置响应头打开方式:content-disposition

        //解决中文文件名问题
        //1.获取user-agent请求头、
        String agent = request.getHeader("user-agent");
        //2.使用工具类方法编码文件名即可
        filename = DownLoadUtils.getFileName(agent, filename);

        response.setHeader("content-disposition","attachment;filename="+filename);
        //4.将输入流的数据写出到输出流中
        ServletOutputStream sos = response.getOutputStream();
        byte[] buff = new byte[1024 * 8];
        int len = 0;
        while((len = fis.read(buff)) != -1){
            sos.write(buff,0,len);
        }
        fis.close();
        return null;
    }
}
