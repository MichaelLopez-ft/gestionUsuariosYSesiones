import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "sesiones")
public class Sesion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha_inicio_sesion", nullable = false)
    private LocalDateTime fechaInicioSesion;

    // Getters y setters

    public Long getId() { return id; }
    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
    public LocalDateTime getFechaInicioSesion() { return fechaInicioSesion; }
    public void setFechaInicioSesion(LocalDateTime fechaInicioSesion) { this.fechaInicioSesion = fechaInicioSesion; }
}
