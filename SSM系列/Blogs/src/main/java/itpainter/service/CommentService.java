package itpainter.service;

import itpainter.mapper.CommentMapper;
import itpainter.mapper.TagMapper;
import itpainter.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {
    @Autowired
    private CommentMapper commentMapper;

    public Map<Comment, List<Comment>> comment(int id) {
        List<Comment> list = commentMapper.comment(id);
        Map<Comment, List<Comment>> map = new HashMap<>();
        for (Comment c : list) {
            boolean flag = false;
            if (c.getParentCommentId() == null) {
                map.put(c, new ArrayList<Comment>());
                flag = true;
            } else {
                int pid = c.getParentCommentId();
                for (Comment set : map.keySet()) {
                    if (set.getId() == pid) {
                        List<Comment> list1 = map.get(set);
                        list1.add(c);
                        map.put(set, list1);
                        flag = true;
                        break;
                    } else {
                        List<Comment> list1 = map.get(set);
                        for (Comment i : list1) {
                            if (i.getId() == pid) {
                                list1.add(c);
                                map.put(set, list1);
                                flag = true;
                                break;
                            }
                        }
                    }
                }
            }
            if (!flag) {
                throw new RuntimeException("没找到论评位置或父评论");
            }
        }
        return map;
    }

}
