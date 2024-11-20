package es.alten.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

@Entity
@XmlRootElement
@EqualsAndHashCode(callSuper = true)
@Data
@Table(name = "images")
public class Image extends Audit {
    @Serial private static final long serialVersionUID = 7040125363430892302L;

    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "type", nullable = false)
    private String type;
    @Lob
    @Column(name = "image_data", nullable = false, length = 10000)
    private byte[] imageData;
}
