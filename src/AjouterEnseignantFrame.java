package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Interface pour ajouter un enseignant
 */
public class AjouterEnseignantFrame extends JFrame {

    // Composants de l'interface
    private JTextField nomField, prenomField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton ajouterButton, annulerButton;
    private JLabel statusLabel;
    
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color lightColor = new Color(240, 240, 240);    // Gris clair pour le fond
    private Color accentColor = new Color(255, 153, 0);     // Orange pour accentuation
    private Color successColor = new Color(46, 204, 113);   // Vert succès
    private Color dangerColor = new Color(231, 76, 60);     // Rouge danger

    public AjouterEnseignantFrame() {
        // Configuration de la fenêtre
        setTitle("Ajouter un Enseignant");
        setSize(750, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(lightColor);
        
        // ====== ENTÊTE ======
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // ====== FORMULAIRE ======
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
        
        // ====== BOUTONS ======
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Afficher la fenêtre
        setVisible(true);
    }
    
    /**
     * Crée le panneau d'entête avec le titre
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("AJOUTER UN NOUVEL ENSEIGNANT");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crée le panneau de formulaire avec les champs
     */
    private JPanel createFormPanel() {
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(lightColor);
        wrapperPanel.setBorder(new EmptyBorder(25, 40, 25, 40));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));
        
        // Informations personnelles
        JLabel sectionLabel1 = new JLabel("Informations personnelles");
        sectionLabel1.setFont(new Font("Arial", Font.BOLD, 14));
        sectionLabel1.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Champ Nom
        JLabel nomLabel = new JLabel("Prénom");
        nomField = new JTextField(20);
        nomField.setPreferredSize(new Dimension(300, 30));
        
        // Champ Prénom
        JLabel prenomLabel = new JLabel("Nom");
        prenomField = new JTextField(20);
        prenomField.setPreferredSize(new Dimension(300, 30));
        
        // Informations de compte
        JLabel sectionLabel2 = new JLabel("Informations de compte");
        sectionLabel2.setFont(new Font("Arial", Font.BOLD, 14));
        sectionLabel2.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        // Champ Email
        JLabel emailLabel = new JLabel("Email académique");
        emailField = new JTextField(20);
        emailField.setPreferredSize(new Dimension(300, 30));
        
        // Champ Mot de passe
        JLabel passwordLabel = new JLabel("Mot de passe");
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(300, 30));
        
        // Champ Confirmation mot de passe
        JLabel confirmPasswordLabel = new JLabel("Confirmer le mot de passe");
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setPreferredSize(new Dimension(300, 30));
        
        // Message de statut
        statusLabel = new JLabel("");
        statusLabel.setForeground(dangerColor);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // Création des panels pour chaque groupe
        JPanel nomPanel = createFieldPanel(nomLabel, nomField);
        JPanel prenomPanel = createFieldPanel(prenomLabel, prenomField);
        JPanel emailPanel = createFieldPanel(emailLabel, emailField);
        JPanel passwordPanel = createFieldPanel(passwordLabel, passwordField);
        JPanel confirmPasswordPanel = createFieldPanel(confirmPasswordLabel, confirmPasswordField);
        
        // Ajout des composants au panel du formulaire
        formPanel.add(sectionLabel1);
        formPanel.add(nomPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(prenomPanel);
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(sectionLabel2);
        formPanel.add(emailPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(passwordPanel);
        formPanel.add(Box.createVerticalStrut(10));
        formPanel.add(confirmPasswordPanel);
        formPanel.add(Box.createVerticalStrut(20));
        formPanel.add(statusLabel);
        
        wrapperPanel.add(formPanel, BorderLayout.CENTER);
        
        return wrapperPanel;
    }
    
    /**
     * Crée un panel pour un champ de formulaire avec son étiquette
     */
    private JPanel createFieldPanel(JLabel label, JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 0, 5, 0));
        
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Crée le panneau des boutons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Bouton Annuler
        annulerButton = new JButton("Annuler");
        annulerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        annulerButton.setPreferredSize(new Dimension(120, 40));
        annulerButton.setFocusPainted(false);
        
        // Bouton Ajouter
        ajouterButton = new JButton("Ajouter");
        ajouterButton.setFont(new Font("Arial", Font.PLAIN, 14));
        ajouterButton.setForeground(Color.WHITE);
        ajouterButton.setBackground(primaryColor);
        ajouterButton.setPreferredSize(new Dimension(120, 40));
        ajouterButton.setFocusPainted(false);
        
        // Effets de survol
        ajouterButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ajouterButton.setBackground(new Color(0, 90, 180));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ajouterButton.setBackground(primaryColor);
            }
        });
        
        // Ajouter les boutons au panel
        buttonPanel.add(annulerButton);
        buttonPanel.add(ajouterButton);
        
        // Actions des boutons
        annulerButton.addActionListener(e -> dispose());
        
        ajouterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ajouterEnseignant();
            }
        });
        
        return buttonPanel;
    }
    
    /**
     * Méthode pour ajouter un enseignant à la base de données
     */
    private void ajouterEnseignant() {
        // Récupération des valeurs des champs
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        
        // Validation des champs
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || password.isEmpty()) {
            statusLabel.setText("Veuillez remplir tous les champs obligatoires");
            statusLabel.setForeground(dangerColor);
            shakeField(nomField, nom.isEmpty());
            shakeField(prenomField, prenom.isEmpty());
            shakeField(emailField, email.isEmpty());
            shakeField(passwordField, password.isEmpty());
            return;
        }
        
        // Validation de l'email
        if (!email.contains("@") || !email.contains(".")) {
            statusLabel.setText("Veuillez entrer une adresse email valide");
            statusLabel.setForeground(dangerColor);
            shakeField(emailField, true);
            return;
        }
        
        // Validation du mot de passe
        if (password.length() < 6) {
            statusLabel.setText("Le mot de passe doit contenir au moins 6 caractères");
            statusLabel.setForeground(dangerColor);
            shakeField(passwordField, true);
            return;
        }
        
        // Validation de la confirmation du mot de passe
        if (!password.equals(confirmPassword)) {
            statusLabel.setText("Les mots de passe ne correspondent pas");
            statusLabel.setForeground(dangerColor);
            shakeField(confirmPasswordField, true);
            return;
        }
        
        // Ajouter l'enseignant à la base de données
        String nomComplet = nom + " " + prenom;
        int userId = Donnees.ajouterUtilisateur(nomComplet, email, password, "enseignant");
        
        if (userId != -1) {
            // Succès
            statusLabel.setText("Enseignant ajouté avec succès !");
            statusLabel.setForeground(successColor);
            
            // Réinitialiser les champs
            nomField.setText("");
            prenomField.setText("");
            emailField.setText("");
            passwordField.setText("");
            confirmPasswordField.setText("");
            
            // Animation de succès
            Timer timer = new Timer(2000, evt -> {
                dispose(); // Fermer la fenêtre après 2 secondes
            });
            timer.setRepeats(false);
            timer.start();
            
        } else {
            // Échec
            statusLabel.setText("Erreur lors de l'ajout de l'enseignant. Veuillez réessayer.");
            statusLabel.setForeground(dangerColor);
        }
    }
    
    /**
     * Animation pour secouer un champ invalide
     */
    private void shakeField(JComponent field, boolean shake) {
        if (!shake) return;
        
        final Point point = field.getLocation();
        final int distance = 10;
        
        Timer timer = new Timer(30, null);
        final int[] moves = {distance, -distance, distance/2, -distance/2, distance/4, -distance/4, 0};
        final int[] currentMove = {0};
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentMove[0] >= moves.length) {
                    timer.stop();
                    field.setLocation(point);
                    return;
                }
                
                field.setLocation(point.x + moves[currentMove[0]], point.y);
                currentMove[0]++;
            }
        });
        
        timer.start();
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
        
        SwingUtilities.invokeLater(() -> new AjouterEnseignantFrame());
    }
}