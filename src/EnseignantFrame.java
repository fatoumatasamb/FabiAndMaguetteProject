package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;

/**
 * Interface principale pour l'enseignant
 */
public class EnseignantFrame extends JFrame {
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    private Color accentColor = new Color(255, 153, 0);     // Orange pour accentuation
    
    // ID de l'utilisateur connecté
    private int userId;

    public EnseignantFrame(String username, int userId) {
        this.userId = userId;
        
        // Configuration de la fenêtre
        setTitle("Enseignant - " + username);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
        
        // ====== ENTÊTE ======
        JPanel headerPanel = createHeaderPanel(username);
        add(headerPanel, BorderLayout.NORTH);
        
        // ====== CONTENU PRINCIPAL ======
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(backgroundColor);
        
        // ====== MENU PRINCIPAL ======
        JPanel menuPanel = createMenuPanel();
        mainPanel.add(menuPanel, BorderLayout.CENTER);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // ====== PIED DE PAGE ======
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
        
        // Afficher la fenêtre
        setVisible(true);
    }
    
    /**
     * Crée le panneau d'entête avec le titre et l'utilisateur connecté
     */
    private JPanel createHeaderPanel(String username) {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("SYSTÈME DE GESTION DU CAHIER DE TEXTE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        JLabel userLabel = new JLabel("Bienvenue, " + username);
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        userLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(userLabel, BorderLayout.EAST);
        
        return headerPanel;
    }
    
    /**
     * Crée le panneau du menu principal
     */
    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridBagLayout());
        menuPanel.setBackground(backgroundColor);
        menuPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        
        // Titre du menu
        JLabel menuTitle = new JLabel("MENU ENSEIGNANT");
        menuTitle.setFont(new Font("Arial", Font.BOLD, 16));
        menuTitle.setForeground(primaryColor);
        
        // Boutons du menu
        JButton btnVoirCours = createMenuButton("Voir mes cours");
        JButton btnAjouterSeance = createMenuButton("Ajouter une séance");
        JButton btnDeconnexion = createMenuButton("Déconnexion");
        btnDeconnexion.setBackground(new Color(220, 53, 69)); // Bouton déconnexion en rouge
        btnDeconnexion.setForeground(Color.WHITE);
        
        // Configuration GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 0, 10, 0);
        gbc.weightx = 1.0;
        
        // Ajouter les composants avec GridBagLayout
        gbc.insets = new Insets(0, 0, 20, 0);
        menuPanel.add(menuTitle, gbc);
        
        gbc.insets = new Insets(10, 0, 10, 0);
        menuPanel.add(btnVoirCours, gbc);
        menuPanel.add(btnAjouterSeance, gbc);
        
        gbc.insets = new Insets(30, 0, 10, 0);
        menuPanel.add(btnDeconnexion, gbc);
        
        // Actions des boutons
        btnVoirCours.addActionListener(e -> new VoirMesCoursFrame(userId));
        btnAjouterSeance.addActionListener(e -> new AjouterSeanceFrame(userId));
        btnDeconnexion.addActionListener(e -> {
            int response = JOptionPane.showConfirmDialog(EnseignantFrame.this, 
                "Êtes-vous sûr de vouloir vous déconnecter ?", 
                "Confirmation", JOptionPane.YES_NO_OPTION);
            
            if (response == JOptionPane.YES_OPTION) {
                new LoginFrame();
                dispose();
            }
        });
        
        return menuPanel;
    }
    
    /**
     * Crée un bouton pour le menu principal avec un style spécifique
     */
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBackground(text.equals("Déconnexion") ? new Color(220, 53, 69) : Color.WHITE);
        button.setForeground(text.equals("Déconnexion") ? Color.WHITE : new Color(50, 50, 50));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(350, 50));
        button.setMaximumSize(new Dimension(350, 50));
        
        // Effet de survol
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            private Color originalBackground;
            
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    originalBackground = button.getBackground();
                    if (text.equals("Déconnexion")) {
                        button.setBackground(new Color(200, 35, 51)); // Rouge plus foncé au survol
                    } else {
                        button.setBackground(new Color(230, 230, 230));
                    }
                }
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    if (text.equals("Déconnexion")) {
                        button.setBackground(new Color(220, 53, 69)); // Revenir au rouge original
                    } else {
                        button.setBackground(Color.WHITE);
                    }
                }
            }
        });
        
        return button;
    }
    
    /**
     * Crée le pied de page
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(new Color(220, 220, 220));
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JLabel copyrightLabel = new JLabel("© 2025 - Université de Thiès - Tous droits réservés");
        copyrightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        copyrightLabel.setForeground(new Color(100, 100, 100));
        copyrightLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        footerPanel.add(copyrightLabel, BorderLayout.CENTER);
        
        return footerPanel;
    }

    public static void main(String[] args) {
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
        
        SwingUtilities.invokeLater(() -> new EnseignantFrame("Binetou Sarr", 2));
    }
}