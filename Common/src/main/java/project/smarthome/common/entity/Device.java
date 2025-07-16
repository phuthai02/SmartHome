package project.smarthome.common.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
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
    @JsonBackReference("type-device")
    private DeviceType type;

    @Column(name = "STATUS")
    private String status;

    @ManyToOne
    @JoinColumn(name = "AREA_ID")
    @JsonBackReference("area-device")
    private Area area;
}
