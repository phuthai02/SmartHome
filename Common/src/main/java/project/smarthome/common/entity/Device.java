package project.smarthome.common.entity;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;

@Data
@Entity
@Table(name = "DEVICE")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TYPE")
    @JsonBackReference
    private DeviceType type;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "AREA_ID")
    @JsonBackReference
    private Area area;
}
