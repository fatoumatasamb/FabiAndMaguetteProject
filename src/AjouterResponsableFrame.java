package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interface pour ajouter un responsable de classe avec création de classe si nécessaire
 */
public class AjouterResponsableFrame extends JFrame {

    // Composants de l'interface
    private JTextField nomField, prenomField, emailField;
    private JPasswordField passwordField, confirmPasswordField;
    private JComboBox<String> classeComboBox;
    private JButton ajouterButton, annulerButton, createClasseButton;
    private JLabel statusLabel;
    
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    private Color accentColor = new Color(255, 153, 0);     // Orange pour accentuation
    private Color successColor = new Color(46, 204, 113);   // Vert succès
    private Color dangerColor = new Color(231, 76, 60);     // Rouge danger
    
    // Stockage des IDs de classe
    private ArrayList<Integer> classeIds = new ArrayList<>();
    
    // État pour indiquer s'il n'y a pas de classes disponibles
    private boolean noClassesAvailable = false;

    public AjouterResponsableFrame() {
        // Configuration de la fenêtre
        setTitle("Ajouter un Responsable de Classe");
        setSize(500, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(true);
        
        // Layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
        
        // ====== ENTÊTE ======
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // ====== FORMULAIRE ======
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);
        
        // ====== BOUTONS ======
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Charger les classes disponibles
        chargerClasses();
        
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
        
        JLabel titleLabel = new JLabel("AJOUTER UN RESPONSABLE DE CLASSE");
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
        wrapperPanel.setBackground(backgroundColor);
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
        JLabel nomLabel = new JLabel("Nom");
        nomField = new JTextField(20);
        nomField.setPreferredSize(new Dimension(300, 30));
        
        // Champ Prénom
        JLabel prenomLabel = new JLabel("Prénom");
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
        
        // Information de classe
        JLabel sectionLabel3 = new JLabel("Affectation à une classe");
        sectionLabel3.setFont(new Font("Arial", Font.BOLD, 14));
        sectionLabel3.setBorder(new EmptyBorder(20, 0, 10, 0));
        
        // Champ Classe
        JLabel classeLabel = new JLabel("Classe à superviser");
        classeComboBox = new JComboBox<>();
        classeComboBox.setPreferredSize(new Dimension(300, 30));
        
        // Bouton pour créer une nouvelle classe si nécessaire
        createClasseButton = new JButton("+ Créer une nouvelle classe");
        createClasseButton.setFont(new Font("Arial", Font.PLAIN, 12));
        createClasseButton.setBackground(new Color(240, 240, 240));
        createClasseButton.setForeground(primaryColor);
        createClasseButton.setBorderPainted(false);
        createClasseButton.setFocusPainted(false);
        createClasseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        createClasseButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        createClasseButton.setOpaque(false);
        
        // Ajouter un écouteur d'événement au bouton de création de classe
        createClasseButton.addActionListener(e -> openCreateClasseDialog());
        
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
        JPanel classePanel = createFieldPanel(classeLabel, classeComboBox);
        
        // Panel pour le bouton de création de classe
        JPanel createClassePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        createClassePanel.setBackground(Color.WHITE);
        createClassePanel.add(createClasseButton);
        
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
        formPanel.add(Box.createVerticalStrut(15));
        
        formPanel.add(sectionLabel3);
        formPanel.add(classePanel);
        formPanel.add(Box.createVerticalStrut(5));
        formPanel.add(createClassePanel);
        formPanel.add(Box.createVerticalStrut(15));
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
                ajouterResponsable();
            }
        });
        
        return buttonPanel;
    }
    
    /**
     * Charge les classes disponibles dans la combobox
     */
    private void chargerClasses() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Donnees.connexion_a_la_base();
            stmt = conn.createStatement();
            String sql = "SELECT id, nom FROM classes WHERE responsable_id IS NULL";
            rs = stmt.executeQuery(sql);
            
            // Ajouter option par défaut
            classeComboBox.removeAllItems();
            classeIds.clear();
            classeComboBox.addItem("- Sélectionner une classe -");
            classeIds.add(0); // ID fictif pour l'option par défaut
            
            int count = 0;
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                classeComboBox.addItem(nom);
                classeIds.add(id);
                count++;
            }
            
            // Vérifier s'il n'y a pas de classes disponibles
            noClassesAvailable = (count == 0);
            if (noClassesAvailable) {
                statusLabel.setText("Aucune classe disponible. Veuillez en créer une nouvelle.");
                statusLabel.setForeground(new Color(255, 153, 0)); // Orange pour avertissement
                
                // Mettre l'accent sur le bouton de création de classe
                createClasseButton.setFont(new Font("Arial", Font.BOLD, 12));
                createClasseButton.setForeground(new Color(255, 153, 0));
            }
            
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des classes: " + e.getMessage());
            statusLabel.setText("Impossible de charger les classes disponibles");
            statusLabel.setForeground(dangerColor);
        } finally {
            Donnees.fermerResultSet(rs);
            Donnees.fermerStatement(stmt);
            Donnees.fermerConnexion(conn);
        }
    }
    
    /**
     * Ouvre une boîte de dialogue pour créer une nouvelle classe
     */
    private void openCreateClasseDialog() {
        JDialog dialog = new JDialog(this, "Créer une nouvelle classe", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());
        
        // Panel du formulaire
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new EmptyBorder(20, 20, 10, 20));
        formPanel.setBackground(Color.WHITE);
        
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.insets = new Insets(5, 5, 5, 5);
        
        // Nom de la classe
        JLabel nomClasseLabel = new JLabel("Nom de la classe:");
        JTextField nomClasseField = new JTextField(20);
        
        // Label de statut
        JLabel dialogStatusLabel = new JLabel("");
        dialogStatusLabel.setForeground(dangerColor);
        
        // Boutons
        JButton cancelButton = new JButton("Annuler");
        JButton createButton = new JButton("Créer");
        createButton.setBackground(primaryColor);
        createButton.setForeground(Color.WHITE);
        
        // Placement des composants
        constraints.gridx = 0;
        constraints.gridy = 0;
        formPanel.add(nomClasseLabel, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        formPanel.add(nomClasseField, constraints);
        
        constraints.gridx = 0;
        constraints.gridy = 2;
        formPanel.add(dialogStatusLabel, constraints);
        
        // Panel des boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(cancelButton);
        buttonPanel.add(createButton);
        
        // Actions des boutons
        cancelButton.addActionListener(e -> dialog.dispose());
        createButton.addActionListener(e -> {
            String nomClasse = nomClasseField.getText().trim();
            if (nomClasse.isEmpty()) {
                dialogStatusLabel.setText("Veuillez entrer un nom de classe");
                return;
            }
            
            // Créer la classe et rafraîchir la liste
            if (creerNouvelleClasse(nomClasse)) {
                dialog.dispose();
                chargerClasses();
                
                // Sélectionner la nouvelle classe
                for (int i = 0; i < classeComboBox.getItemCount(); i++) {
                    if (classeComboBox.getItemAt(i).equals(nomClasse)) {
                        classeComboBox.setSelectedIndex(i);
                        break;
                    }
                }
            } else {
                dialogStatusLabel.setText("Erreur lors de la création de la classe");
            }
        });
        
        // Ajouter les panels à la boîte de dialogue
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        // Afficher la boîte de dialogue
        dialog.setVisible(true);
    }
    
    /**
     * Crée une nouvelle classe dans la base de données
     */
    private boolean creerNouvelleClasse(String nomClasse) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        boolean success = false;
        
        try {
            conn = Donnees.connexion_a_la_base();
            String sql = "INSERT INTO classes (nom) VALUES (?)";
            stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, nomClasse);
            
            int rowsAffected = stmt.executeUpdate();
            success = (rowsAffected > 0);
            
            if (success) {
                // Récupérer l'ID de la nouvelle classe
                rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int newClassId = rs.getInt(1);
                    // Ajouter la nouvelle classe à la liste
                    classeIds.add(newClassId);
                }
            }
        } catch (Exception e) {
            System.out.println("Erreur lors de la création de la classe: " + e.getMessage());
            e.printStackTrace();
            success = false;
        } finally {
            Donnees.fermerResultSet(rs);
            Donnees.fermerStatement(stmt);
            Donnees.fermerConnexion(conn);
        }
        
        return success;
    }
    
    /**
     * Méthode pour ajouter un responsable à la base de données et l'associer à une classe
     */
    private void ajouterResponsable() {
        // Récupération des valeurs des champs
        String nom = nomField.getText().trim();
        String prenom = prenomField.getText().trim();
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String confirmPassword = new String(confirmPasswordField.getPassword()).trim();
        int selectedIndex = classeComboBox.getSelectedIndex();
        
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
        
        // Validation de la classe (sauf si aucune classe n'est disponible)
        if (selectedIndex <= 0 && !noClassesAvailable) {
            statusLabel.setText("Veuillez sélectionner une classe ou en créer une nouvelle");
            statusLabel.setForeground(dangerColor);
            shakeField(classeComboBox, true);
            return;
        }
        
        Connection conn = null;
        PreparedStatement stmtUser = null;
        PreparedStatement stmtClasse = null;
        
        try {
            // Ajouter l'utilisateur et récupérer son ID
            String nomComplet = nom + " " + prenom;
            int userId = Donnees.ajouterUtilisateur(nomComplet, email, password, "responsable");
            
            if (userId != -1) {
                // Associer l'utilisateur à la classe
                conn = Donnees.connexion_a_la_base();
                
                // Si aucune classe n'a été sélectionnée mais qu'aucune n'est disponible,
                // inviter l'utilisateur à en créer une
                if (selectedIndex <= 0 && noClassesAvailable) {
                    statusLabel.setText("Veuillez créer une classe pour ce responsable");
                    statusLabel.setForeground(new Color(255, 153, 0));
                    return;
                }
                
                String sql = "UPDATE classes SET responsable_id = ? WHERE id = ?";
                stmtClasse = conn.prepareStatement(sql);
                stmtClasse.setInt(1, userId);
                stmtClasse.setInt(2, classeIds.get(selectedIndex));
                stmtClasse.executeUpdate();
                
                // Succès
                statusLabel.setText("Responsable ajouté avec succès !");
                statusLabel.setForeground(successColor);
                
                // Réinitialiser les champs
                nomField.setText("");
                prenomField.setText("");
                emailField.setText("");
                passwordField.setText("");
                confirmPasswordField.setText("");
                classeComboBox.setSelectedIndex(0);
                
                // Recharger les classes
                chargerClasses();
                
                // Animation de succès
                Timer timer = new Timer(2000, evt -> {
                    dispose(); // Fermer la fenêtre après 2 secondes
                });
                timer.setRepeats(false);
                timer.start();
                
            } else {
                // Échec
                statusLabel.setText("Erreur lors de l'ajout du responsable. Veuillez réessayer.");
                statusLabel.setForeground(dangerColor);
            }
        } catch (Exception e) {
            statusLabel.setText("Erreur: " + e.getMessage());
            statusLabel.setForeground(dangerColor);
            e.printStackTrace();
        } finally {
            Donnees.fermerStatement(stmtUser);
            Donnees.fermerStatement(stmtClasse);
            Donnees.fermerConnexion(conn);
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
        
        SwingUtilities.invokeLater(() -> new AjouterResponsableFrame());
    }
}