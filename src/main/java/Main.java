import java.util.List;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class Main {
    private static UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static SesionDAO sesionDAO = new SesionDAO();

    private static JPanel panelPrincipal;
    private static JPanel panelCentral;
    private static JPanel panelBotones;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Gestión de Usuarios y Sesiones");
        frame.setSize(700, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout(10, 10));

        // Panel superior con un título
        JPanel panelTitulo = new JPanel();
        JLabel titulo = new JLabel("Bienvenido a la Gestión de Usuarios y Sesiones", JLabel.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        panelTitulo.add(titulo);

        // Panel central que contendrá el contenido dinámico
        panelCentral = new JPanel();
        panelCentral.setLayout(new BorderLayout());
        panelCentral.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Panel inferior con los botones
        panelBotones = new JPanel();
        panelBotones.setLayout(new GridLayout(1, 4, 10, 10));
        JButton btnInsertarUsuarioYSesion = new JButton("Insertar Usuario y Sesión");
        JButton btnMostrarUsuarios = new JButton("Mostrar Usuarios");
        JButton btnMostrarSesiones = new JButton("Mostrar Sesiones");
        JButton btnSalir = new JButton("Salir");

        panelBotones.add(btnInsertarUsuarioYSesion);
        panelBotones.add(btnMostrarUsuarios);
        panelBotones.add(btnMostrarSesiones);
        panelBotones.add(btnSalir);

        frame.add(panelTitulo, BorderLayout.NORTH);
        frame.add(panelCentral, BorderLayout.CENTER);
        frame.add(panelBotones, BorderLayout.SOUTH);

        // Acciones de los botones
        btnInsertarUsuarioYSesion.addActionListener(e -> mostrarPanelInsertarUsuario());
        btnMostrarUsuarios.addActionListener(e -> mostrarUsuarios());
        btnMostrarSesiones.addActionListener(e -> mostrarSesiones());
        btnSalir.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Sesión Finalizada.");
            System.exit(0);
        });

        frame.setVisible(true);
    }

    private static void mostrarPanelInsertarUsuario() {
        panelCentral.removeAll();

        JPanel panelForm = new JPanel();
        panelForm.setLayout(new GridLayout(4, 2, 10, 10));

        JTextField nombreField = new JTextField(20);
        JTextField correoField = new JTextField(20);
        LocalDate fechaActual = LocalDate.now();
        String fechaFormateada = fechaActual.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        JTextField fechaField = new JTextField(fechaFormateada);
        fechaField.setEditable(false);
        JButton btnGuardar = new JButton("Guardar");

        panelForm.add(new JLabel("Nombre:"));
        panelForm.add(nombreField);
        panelForm.add(new JLabel("Correo:"));
        panelForm.add(correoField);
        panelForm.add(new JLabel("Fecha:"));
        panelForm.add(fechaField);
        panelForm.add(new JLabel("")); // Espacio vacío
        panelForm.add(btnGuardar);

        panelCentral.add(panelForm, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();

        btnGuardar.addActionListener(ev -> {
            String nombre = nombreField.getText();
            String correo = correoField.getText();
            if (!nombre.isEmpty() && !correo.isEmpty()) {
                Usuario usuario = new Usuario();
                usuario.setNombre(nombre);
                usuario.setCorreo(correo);
                Sesion sesion = new Sesion();
                sesion.setFechaInicioSesion(LocalDateTime.now());
                sesion.setUsuario(usuario);
                usuario.getSesiones().add(sesion);
                usuarioDAO.insertarUsuario(usuario);
                sesionDAO.insertarSesion(sesion);
                JOptionPane.showMessageDialog(panelPrincipal, "Usuario y sesión insertados exitosamente.");
            } else {
                JOptionPane.showMessageDialog(panelPrincipal, "Todos los campos son obligatorios.");
            }
        });
    }

    private static void mostrarUsuarios() {
        panelCentral.removeAll();

        List<Usuario> usuarios = usuarioDAO.obtenerUsuarios();
        String[] columnNames = {"ID", "Nombre", "Correo"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Usuario usuario : usuarios) {
            Object[] row = {usuario.getId(), usuario.getNombre(), usuario.getCorreo()};
            model.addRow(row);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Usuarios"));

        panelCentral.add(scrollPane, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }

    private static void mostrarSesiones() {
        panelCentral.removeAll();

        List<Sesion> sesiones = sesionDAO.obtenerSesiones();
        String[] columnNames = {"ID Sesión", "ID Usuario", "Fecha Inicio"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        for (Sesion sesion : sesiones) {
            Object[] row = {sesion.getId(), sesion.getUsuario().getId(), sesion.getFechaInicioSesion()};
            model.addRow(row);
        }

        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Lista de Sesiones"));

        panelCentral.add(scrollPane, BorderLayout.CENTER);
        panelCentral.revalidate();
        panelCentral.repaint();
    }
}

