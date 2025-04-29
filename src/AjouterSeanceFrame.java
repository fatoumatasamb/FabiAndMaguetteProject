package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * Interface pour ajouter une séance à un cours
 */
public class AjouterSeanceFrame extends JFrame {
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    private Color successColor = new Color(46, 204, 113);   // Vert succès
    private Color dangerColor = new Color(231, 76, 60);     // Rouge danger
    
    // Composants de l'interface
    private JComboBox<String> coursComboBox;
    private JTextField dateField;
    private JTextArea contenuArea;
    private JButton ajouterButton, annulerButton;
    private JLabel statusLabel;
    
    // ID de l'enseignant
    private int enseignantId;
    
    // Stockage des IDs des cours
    private ArrayList<Integer> coursIds = new ArrayList<>();

    public AjouterSeanceFrame(int enseignantId) {
        this.enseignantId = enseignantId;
        
        // Configuration de la fenêtre
        setTitle("Ajouter une séance");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
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
        
        // Charger les cours de l'enseignant
        chargerCours();
        
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
        
        JLabel titleLabel = new JLabel("AJOUTER UNE SÉANCE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crée le panneau du formulaire
     */
    private JPanel createFormPanel() {
        JPanel wrapperPanel = new JPanel(new BorderLayout());
        wrapperPanel.setBackground(backgroundColor);
        wrapperPanel.setBorder(new EmptyBorder(20, 40, 20, 40));
        
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        // Titre du formulaire
        JLabel formTitleLabel = new JLabel("Informations de la séance");
        formTitleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        formTitleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Sélection du cours
        JLabel coursLabel = new JLabel("Cours");
        coursComboBox = new JComboBox<>();
        coursComboBox.setPreferredSize(new Dimension(300, 30));
        
        // Date de la séance
        JLabel dateLabel = new JLabel("Date (YYYY-MM-DD)");
        dateField = new JTextField(20);
        dateField.setPreferredSize(new Dimension(300, 30));
        
        // Date actuelle par défaut
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        dateField.setText(sdf.format(new Date()));
        
        // Contenu de la séance
        JLabel contenuLabel = new JLabel("Contenu de la séance");
        contenuArea = new JTextArea(12, 20);
        contenuArea.setLineWrap(true);
        contenuArea.setWrapStyleWord(true);
        contenuArea.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        JScrollPane scrollPane = new JScrollPane(contenuArea);
        
        // Message de statut
        statusLabel = new JLabel("");
        statusLabel.setForeground(dangerColor);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        // Création des panels pour chaque groupe
        JPanel coursPanel = createFieldPanel(coursLabel, coursComboBox);
        JPanel datePanel = createFieldPanel(dateLabel, dateField);
        JPanel contenuPanel = createFieldPanel(contenuLabel, scrollPane);
        
        // Ajout des composants au panel du formulaire
        formPanel.add(formTitleLabel);
        formPanel.add(coursPanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(datePanel);
        formPanel.add(Box.createVerticalStrut(15));
        formPanel.add(contenuPanel);
        formPanel.add(Box.createVerticalStrut(10));
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
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Bouton Annuler
        annulerButton = new JButton("Annuler");
        annulerButton.setFont(new Font("Arial", Font.BOLD, 14));
        annulerButton.setPreferredSize(new Dimension(150, 40));
        annulerButton.setFocusPainted(false);
        annulerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bouton Ajouter
        ajouterButton = new JButton("Ajouter la séance");
        ajouterButton.setFont(new Font("Arial", Font.BOLD, 14));
        ajouterButton.setForeground(Color.WHITE);
        ajouterButton.setBackground(primaryColor);
        ajouterButton.setPreferredSize(new Dimension(180, 40));
        ajouterButton.setFocusPainted(false);
        ajouterButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Ajouter les boutons au panel
        buttonPanel.add(annulerButton);
        buttonPanel.add(ajouterButton);
        
        // Actions des boutons
        annulerButton.addActionListener(e -> dispose());
        ajouterButton.addActionListener(e -> ajouterSeance());
        
        // Effet de survol
        ajouterButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ajouterButton.setBackground(new Color(0, 90, 180));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ajouterButton.setBackground(primaryColor);
            }
        });
        
        return buttonPanel;
    }
    
    /**
     * Charge les cours de l'enseignant dans la liste déroulante
     */
    private void chargerCours() {
        // Récupérer les cours de l'enseignant
        ArrayList<Map<String, Object>> cours = Donnees.getCoursEnseignant(enseignantId);
        
        // Ajouter option par défaut
        coursComboBox.addItem("- Sélectionner un cours -");
        coursIds.add(0); // ID fictif pour l'option par défaut
        
        // Remplir la liste avec les cours
        for (Map<String, Object> unCours : cours) {
            int id = (int) unCours.get("id");
            String nom = (String) unCours.get("nom");
            String classeNom = (String) unCours.get("classe_nom");
            
            coursComboBox.addItem(nom + " (" + classeNom + ")");
            coursIds.add(id);
        }
        
        // Désactiver le bouton si aucun cours n'est disponible
        ajouterButton.setEnabled(cours.size() > 0);
        if (cours.size() == 0) {
            statusLabel.setText("Aucun cours à votre nom. Contactez le chef de département.");
            statusLabel.setForeground(dangerColor);
        }
    }
    
    /**
     * Méthode pour ajouter une séance
     */
    private void ajouterSeance() {
        // Récupération des valeurs
        int coursIndex = coursComboBox.getSelectedIndex();
        String dateStr = dateField.getText().trim();
        String contenu = contenuArea.getText().trim();
        
        // Validation des champs
        if (coursIndex <= 0) {
            statusLabel.setText("Veuillez sélectionner un cours");
            statusLabel.setForeground(dangerColor);
            shakeComponent(coursComboBox);
            return;
        }
        
        if (dateStr.isEmpty()) {
            statusLabel.setText("Veuillez entrer une date");
            statusLabel.setForeground(dangerColor);
            shakeComponent(dateField);
            return;
        }
        
        // Validation du format de la date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            statusLabel.setText("Format de date invalide (YYYY-MM-DD)");
            statusLabel.setForeground(dangerColor);
            shakeComponent(dateField);
            return;
        }
        
        if (contenu.isEmpty()) {
            statusLabel.setText("Veuillez entrer le contenu de la séance");
            statusLabel.setForeground(dangerColor);
            shakeComponent(contenuArea);
            return;
        }
        
        // Conversion de la date au format SQL
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        
        // Ajouter la séance dans la base de données
        int coursId = coursIds.get(coursIndex);
        boolean success = Donnees.ajouterSeance(coursId, sqlDate, contenu);
        
        if (success) {
            // Succès
            statusLabel.setText("Séance ajoutée avec succès !");
            statusLabel.setForeground(successColor);
            
            // Réinitialiser les champs
            coursComboBox.setSelectedIndex(0);
            dateField.setText(sdf.format(new Date())); // Date actuelle
            contenuArea.setText("");
            
            // Animation de succès
            Timer timer = new Timer(2000, evt -> {
                dispose(); // Fermer la fenêtre après 2 secondes
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            // Échec
            statusLabel.setText("Erreur lors de l'ajout de la séance");
            statusLabel.setForeground(dangerColor);
        }
    }
    
    /**
     * Animation pour secouer un composant invalide
     */
    private void shakeComponent(Component component) {
        final Point point = component.getLocation();
        final int distance = 10;
        
        Timer timer = new Timer(30, null);
        final int[] moves = {distance, -distance, distance/2, -distance/2, distance/4, -distance/4, 0};
        final int[] currentMove = {0};
        
        timer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentMove[0] >= moves.length) {
                    timer.stop();
                    component.setLocation(point);
                    return;
                }
                
                component.setLocation(point.x + moves[currentMove[0]], point.y);
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
        
        SwingUtilities.invokeLater(() -> new AjouterSeanceFrame(2));
    }
}