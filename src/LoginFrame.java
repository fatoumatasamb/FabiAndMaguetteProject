package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

/**
 * Interface d'authentification pour accéder à l'application
 */
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;
    private Color primaryColor = new Color(0, 102, 204); // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    private Color accentColor = new Color(255, 153, 0); // Orange pour accentuation

    public LoginFrame() {
        // Configuration de la fenêtre
        setTitle("Connexion - Gestion du Cahier de Texte");
        setSize(450, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Définir l'icône de l'application si disponible
        // setIconImage(new ImageIcon("path/to/icon.png").getImage());
        
        // Utilisation du BorderLayout
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
        
        // Panel du haut avec logo et titre
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Logo (simulé avec un JLabel)
        JLabel logoLabel = new JLabel("CAHIER DE TEXTE");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(logoLabel, BorderLayout.CENTER);
        
        // Panel principal avec formulaire
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(backgroundColor);
        formPanel.setBorder(new EmptyBorder(30, 40, 30, 40));
        
        // Titre du formulaire
        JLabel titleLabel = new JLabel("Connexion à votre compte");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(new Color(50, 50, 50));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Espacement
        Component verticalSpace1 = Box.createVerticalStrut(25);
        
        // Label et champ pour l'email
        JPanel emailPanel = new JPanel(new BorderLayout(0, 5));
        emailPanel.setBackground(backgroundColor);
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usernameField = new JTextField();
        usernameField.setPreferredSize(new Dimension(300, 35));
        usernameField.setFont(new Font("Arial", Font.PLAIN, 14));
        emailPanel.add(emailLabel, BorderLayout.NORTH);
        emailPanel.add(usernameField, BorderLayout.CENTER);
        
        // Espacement
        Component verticalSpace2 = Box.createVerticalStrut(15);
        
        // Label et champ pour le mot de passe
        JPanel passwordPanel = new JPanel(new BorderLayout(0, 5));
        passwordPanel.setBackground(backgroundColor);
        JLabel passwordLabel = new JLabel("Mot de passe");
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(300, 35));
        passwordField.setFont(new Font("Arial", Font.PLAIN, 14));
        passwordPanel.add(passwordLabel, BorderLayout.NORTH);
        passwordPanel.add(passwordField, BorderLayout.CENTER);
        
        // Espacement
        Component verticalSpace3 = Box.createVerticalStrut(30);
        
        // Bouton de connexion stylisé
        loginButton = new JButton("SE CONNECTER");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        loginButton.setForeground(Color.WHITE);
        loginButton.setBackground(primaryColor);
        loginButton.setFocusPainted(false);
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginButton.setMaximumSize(new Dimension(300, 40));
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Label pour les messages d'erreur
        statusLabel = new JLabel("");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel.setForeground(Color.RED);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Ajout de tous les composants au panel du formulaire
        formPanel.add(titleLabel);
        formPanel.add(verticalSpace1);
        formPanel.add(emailPanel);
        formPanel.add(verticalSpace2);
        formPanel.add(passwordPanel);
        formPanel.add(verticalSpace3);
        formPanel.add(loginButton);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(statusLabel);
        
        // Panel du bas pour le copyright ou autres infos
        JPanel footerPanel = new JPanel();
        footerPanel.setBackground(new Color(220, 220, 220));
        footerPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        JLabel footerLabel = new JLabel("© 2025 - Université de Thiès");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerLabel.setForeground(new Color(100, 100, 100));
        footerPanel.add(footerLabel);
        
        // Ajout des panels à la fenêtre principale
        add(headerPanel, BorderLayout.NORTH);
        add(formPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
        
        // Configuration de l'action du bouton de connexion
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Désactiver le bouton pendant l'authentification
                loginButton.setEnabled(false);
                loginButton.setText("CONNEXION EN COURS...");
                
                // Utiliser SwingWorker pour ne pas bloquer l'interface
                SwingWorker<Map<String, Object>, Void> worker = new SwingWorker<Map<String, Object>, Void>() {
                    @Override
                    protected Map<String, Object> doInBackground() {
                        String username = usernameField.getText().trim();
                        String password = new String(passwordField.getPassword()).trim();
                        return Donnees.authentifierUtilisateur(username, password);
                    }
                    
                    @Override
                    protected void done() {
                        try {
                            Map<String, Object> user = get();
                            if (user != null) {
                                // Authentification réussie
                                String role = (String) user.get("role");
                                int userId = (int) user.get("id");
                                String nom = (String) user.get("nom");
                                
                                // Redirection selon le rôle
                                switch (role) {
                                    case "chef":
                                        new ChefDepartementFrame(nom, userId);
                                        break;
                                        
                                    case "enseignant":
                                        new EnseignantFrame(nom, userId);
                                        break;
                                        
                                    case "responsable":
                                        new ResponsableClasseFrame(nom, userId);
                                        break;
                                        
                                    default:
                                        statusLabel.setText("Rôle non reconnu");
                                        loginButton.setEnabled(true);
                                        loginButton.setText("SE CONNECTER");
                                        return;
                                }
                                
                                // Fermer la fenêtre de connexion
                                dispose();
                                
                            } else {
                                // Échec d'authentification
                                statusLabel.setText("Email ou mot de passe incorrect");
                                passwordField.setText(""); // Effacer le mot de passe
                                loginButton.setEnabled(true);
                                loginButton.setText("SE CONNECTER");
                                
                                // Secouer légèrement la fenêtre pour indiquer une erreur
                                shakeWindow();
                            }
                        } catch (Exception e) {
                            statusLabel.setText("Erreur de connexion");
                            loginButton.setEnabled(true);
                            loginButton.setText("SE CONNECTER");
                        }
                    }
                };
                
                worker.execute();
            }
        });
        
        // Permettre de se connecter avec la touche Entrée
        getRootPane().setDefaultButton(loginButton);
        
        setVisible(true);
    }
    
    /**
     * Animation simple pour secouer la fenêtre en cas d'erreur
     */
    private void shakeWindow() {
        final int originalX = getLocationOnScreen().x;
        final int originalY = getLocationOnScreen().y;
        
        Timer timer = new Timer(30, null);
        final int[] moves = {-10, 10, -8, 8, -5, 5, -3, 3, 0};
        final int[] currentMove = {0};
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentMove[0] >= moves.length) {
                    timer.stop();
                    setLocation(originalX, originalY);
                    return;
                }
                
                setLocation(originalX + moves[currentMove[0]], originalY);
                currentMove[0]++;
            }
        });
        
        timer.start();
    }

    public static void main(String[] args) {
        // Définir l'apparence et le thème
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        // Lancer l'interface de connexion
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame();
            }
        });
    }
}