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
 * Interface pour assigner un cours à un enseignant
 */
public class AssignerCoursFrame extends JFrame {

    // Composants de l'interface
    private JTextField nomCoursField;
    private JComboBox<String> enseignantComboBox;
    private JComboBox<String> classeComboBox;
    private JButton assignerButton, annulerButton;
    private JLabel statusLabel;
    
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    private Color accentColor = new Color(255, 153, 0);     // Orange pour accentuation
    private Color successColor = new Color(46, 204, 113);   // Vert succès
    private Color dangerColor = new Color(231, 76, 60);     // Rouge danger
    
    // Stockage des IDs
    private ArrayList<Integer> enseignantIds = new ArrayList<>();
    private ArrayList<Integer> classeIds = new ArrayList<>();
    
    // Connexion à la base de données
    private Connection connection;

    public AssignerCoursFrame(Connection connection) {
        this.connection = connection;
        
        // Configuration de la fenêtre
        setTitle("Assigner un Cours");
        setSize(750, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
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
        
        // Charger les enseignants et classes
        chargerEnseignants();
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
        
        JLabel titleLabel = new JLabel("ASSIGNER UN COURS À UN ENSEIGNANT");
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
        
        // Titre du formulaire
        JLabel formTitleLabel = new JLabel("Informations du cours");
        formTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formTitleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Champ Nom du cours
        JLabel nomCoursLabel = new JLabel("Nom du cours");
        nomCoursField = new JTextField(20);
        nomCoursField.setPreferredSize(new Dimension(300, 30));
        
        // Champ Enseignant
        JLabel enseignantLabel = new JLabel("Enseignant");
        enseignantComboBox = new JComboBox<>();
        enseignantComboBox.setPreferredSize(new Dimension(300, 30));
        
        // Champ Classe
        JLabel classeLabel = new JLabel("Classe");
        classeComboBox = new JComboBox<>();
        classeComboBox.setPreferredSize(new Dimension(300, 30));
        
        // Message de statut
        statusLabel = new JLabel("");
        statusLabel.setForeground(dangerColor);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // Création des panels pour chaque groupe
        JPanel nomCoursPanel = createFieldPanel(nomCoursLabel, nomCoursField);
        JPanel enseignantPanel = createFieldPanel(enseignantLabel, enseignantComboBox);
        JPanel classePanel = createFieldPanel(classeLabel, classeComboBox);
        
        // Ajout des composants au panel du formulaire
        formPanel.add(formTitleLabel);
        formPanel.add(nomCoursPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(enseignantPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(classePanel);
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
        
        // Bouton Assigner
        assignerButton = new JButton("Assigner");
        assignerButton.setFont(new Font("Arial", Font.PLAIN, 14));
        assignerButton.setForeground(Color.WHITE);
        assignerButton.setBackground(primaryColor);
        assignerButton.setPreferredSize(new Dimension(120, 40));
        assignerButton.setFocusPainted(false);
        
        // Effets de survol
        assignerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                assignerButton.setBackground(new Color(0, 90, 180));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                assignerButton.setBackground(primaryColor);
            }
        });
        
        // Ajouter les boutons au panel
        buttonPanel.add(annulerButton);
        buttonPanel.add(assignerButton);
        
        // Actions des boutons
        annulerButton.addActionListener(e -> dispose());
        
        assignerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                assignerCours();
            }
        });
        
        return buttonPanel;
    }
    
    /**
     * Charge les enseignants dans la combobox
     */
    private void chargerEnseignants() {
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = connection.createStatement();
            String sql = "SELECT id, nom FROM users WHERE role = 'enseignant'";
            rs = stmt.executeQuery(sql);
            
            // Ajouter option par défaut
            enseignantComboBox.addItem("- Sélectionner un enseignant -");
            enseignantIds.add(0); // ID fictif pour l'option par défaut
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                enseignantComboBox.addItem(nom);
                enseignantIds.add(id);
            }
            
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des enseignants: " + e.getMessage());
            statusLabel.setText("Impossible de charger les enseignants");
            statusLabel.setForeground(dangerColor);
        } finally {
            Donnees.fermerResultSet(rs);
            Donnees.fermerStatement(stmt);
        }
    }
    
    /**
     * Charge les classes dans la combobox
     */
    private void chargerClasses() {
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            stmt = connection.createStatement();
            String sql = "SELECT id, nom FROM classes";
            rs = stmt.executeQuery(sql);
            
            // Ajouter option par défaut
            classeComboBox.addItem("- Sélectionner une classe -");
            classeIds.add(0); // ID fictif pour l'option par défaut
            
            while (rs.next()) {
                int id = rs.getInt("id");
                String nom = rs.getString("nom");
                classeComboBox.addItem(nom);
                classeIds.add(id);
            }
            
        } catch (Exception e) {
            System.out.println("Erreur lors du chargement des classes: " + e.getMessage());
            statusLabel.setText("Impossible de charger les classes");
            statusLabel.setForeground(dangerColor);
        } finally {
            Donnees.fermerResultSet(rs);
            Donnees.fermerStatement(stmt);
        }
    }
    
    /**
     * Méthode pour assigner un cours à un enseignant
     */
    private void assignerCours() {
        // Récupération des valeurs des champs
        String nomCours = nomCoursField.getText().trim();
        int enseignantIndex = enseignantComboBox.getSelectedIndex();
        int classeIndex = classeComboBox.getSelectedIndex();
        
        // Validation des champs
        if (nomCours.isEmpty()) {
            statusLabel.setText("Veuillez entrer un nom de cours");
            statusLabel.setForeground(dangerColor);
            shakeField(nomCoursField, true);
            return;
        }
        
        if (enseignantIndex <= 0) {
            statusLabel.setText("Veuillez sélectionner un enseignant");
            statusLabel.setForeground(dangerColor);
            shakeField(enseignantComboBox, true);
            return;
        }
        
        if (classeIndex <= 0) {
            statusLabel.setText("Veuillez sélectionner une classe");
            statusLabel.setForeground(dangerColor);
            shakeField(classeComboBox, true);
            return;
        }
        
        PreparedStatement stmt = null;
        
        try {
            // Ajouter le cours dans la base de données
            String sql = "INSERT INTO cours (nom, enseignant_id, classe_id) VALUES (?, ?, ?)";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, nomCours);
            stmt.setInt(2, enseignantIds.get(enseignantIndex));
            stmt.setInt(3, classeIds.get(classeIndex));
            
            int rowsAffected = stmt.executeUpdate();
            
            if (rowsAffected > 0) {
                // Succès
                statusLabel.setText("Cours assigné avec succès !");
                statusLabel.setForeground(successColor);
                
                // Réinitialiser les champs
                nomCoursField.setText("");
                enseignantComboBox.setSelectedIndex(0);
                classeComboBox.setSelectedIndex(0);
                
                // Animation de succès
                Timer timer = new Timer(2000, evt -> {
                    dispose(); // Fermer la fenêtre après 2 secondes
                });
                timer.setRepeats(false);
                timer.start();
                
            } else {
                // Échec
                statusLabel.setText("Erreur lors de l'assignation du cours. Veuillez réessayer.");
                statusLabel.setForeground(dangerColor);
            }
        } catch (Exception e) {
            statusLabel.setText("Erreur: " + e.getMessage());
            statusLabel.setForeground(dangerColor);
            e.printStackTrace();
        } finally {
            Donnees.fermerStatement(stmt);
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
        
        SwingUtilities.invokeLater(() -> {
            Connection conn = Donnees.connexion_a_la_base();
            new AssignerCoursFrame(conn);
        });
    }
}