package itpainter.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@Setter
public class MyFile {
    private String name;
    private String file;
    public MyFile(String name, String file){
        this.name=name;
        this.file=file;
    }
}
