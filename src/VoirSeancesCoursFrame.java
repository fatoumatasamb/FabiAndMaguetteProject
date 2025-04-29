package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;
import java.sql.Date;

/**
 * Interface pour visualiser les séances d'un cours pour un enseignant
 */
public class VoirSeancesCoursFrame extends JFrame {
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    private Color validColor = new Color(46, 204, 113);     // Vert pour validé
    private Color pendingColor = new Color(230, 126, 34);   // Orange pour en attente
    
    // Composants de l'interface
    private JTable seancesTable;
    private DefaultTableModel tableModel;
    private JButton ajouterSeanceButton;
    private JButton fermerButton;
    
    // ID et nom du cours
    private int coursId;
    private String coursNom;

    public VoirSeancesCoursFrame(int coursId, String coursNom) {
        this.coursId = coursId;
        this.coursNom = coursNom;
        
        // Configuration de la fenêtre
        setTitle("Séances du cours: " + coursNom);
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
        
        // ====== ENTÊTE ======
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // ====== TABLEAU DES SÉANCES ======
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // ====== PANEL DES BOUTONS ======
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Charger les séances
        chargerSeances();
        
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
        
        JLabel titleLabel = new JLabel("SÉANCES DU COURS: " + coursNom.toUpperCase());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crée le panneau du tableau des séances
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // En-tête du tableau
        String[] columns = {"ID", "Date", "Contenu", "Statut"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Tableau non éditable
            }
        };
        
        // Création du tableau
        seancesTable = new JTable(tableModel);
        seancesTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        seancesTable.setRowHeight(30);
        seancesTable.setFont(new Font("Arial", Font.PLAIN, 14));
        seancesTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        seancesTable.getTableHeader().setBackground(new Color(220, 220, 220));
        seancesTable.getTableHeader().setForeground(new Color(50, 50, 50));
        
        // Masquer la colonne ID
        seancesTable.getColumnModel().getColumn(0).setMinWidth(0);
        seancesTable.getColumnModel().getColumn(0).setMaxWidth(0);
        seancesTable.getColumnModel().getColumn(0).setWidth(0);
        
        // Ajuster les largeurs des colonnes
        seancesTable.getColumnModel().getColumn(1).setPreferredWidth(100);  // Date
        seancesTable.getColumnModel().getColumn(2).setPreferredWidth(450);  // Contenu
        seancesTable.getColumnModel().getColumn(3).setPreferredWidth(100);  // Statut
        
        // Renderer personnalisé pour colorer les statuts
        seancesTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (value != null) {
                    String status = (String) value;
                    if (status.equals("Validée")) {
                        c.setForeground(validColor);
                    } else {
                        c.setForeground(pendingColor);
                    }
                }
                
                return c;
            }
        });
        
        // Ajouter le tableau à un ScrollPane
        JScrollPane scrollPane = new JScrollPane(seancesTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        // Ajouter le ScrollPane au panel
        tablePanel.add(scrollPane, BorderLayout.CENTER);
        
        return tablePanel;
    }
    
    /**
     * Crée le panneau des boutons d'action
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        buttonPanel.setBackground(new Color(245, 245, 245));
        buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));
        
        // Bouton Ajouter séance
        ajouterSeanceButton = new JButton("Ajouter une séance");
        ajouterSeanceButton.setFont(new Font("Arial", Font.BOLD, 14));
        ajouterSeanceButton.setForeground(Color.WHITE);
        ajouterSeanceButton.setBackground(primaryColor);
        ajouterSeanceButton.setPreferredSize(new Dimension(180, 40));
        ajouterSeanceButton.setFocusPainted(false);
        ajouterSeanceButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bouton Fermer
        fermerButton = new JButton("Fermer");
        fermerButton.setFont(new Font("Arial", Font.BOLD, 14));
        fermerButton.setPreferredSize(new Dimension(150, 40));
        fermerButton.setFocusPainted(false);
        fermerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Ajouter les boutons au panel
        buttonPanel.add(ajouterSeanceButton);
        buttonPanel.add(fermerButton);
        
        // Actions des boutons
        ajouterSeanceButton.addActionListener(e -> ajouterNouvelleSeance());
        fermerButton.addActionListener(e -> dispose());
        
        // Effet de survol
        ajouterSeanceButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                ajouterSeanceButton.setBackground(new Color(0, 90, 180));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ajouterSeanceButton.setBackground(primaryColor);
            }
        });
        
        return buttonPanel;
    }
    
    /**
     * Charge les séances du cours dans le tableau
     */
    private void chargerSeances() {
        // Nettoyer le tableau
        tableModel.setRowCount(0);
        
        // Récupérer les séances du cours
        ArrayList<Map<String, Object>> seances = Donnees.getSeancesCours(coursId);
        
        // Remplir le tableau avec les séances
        for (Map<String, Object> seance : seances) {
            Date dateSeance = (Date) seance.get("date");
            boolean validee = (boolean) seance.get("validee");
            
            Object[] row = {
                seance.get("id"),
                dateSeance.toString(),
                seance.get("contenu"),
                validee ? "Validée" : "En attente"
            };
            tableModel.addRow(row);
        }
        
        // Message si aucune séance n'est disponible
        if (seances.isEmpty()) {
            JLabel messageLabel = new JLabel("Aucune séance n'a été ajoutée pour ce cours.");
            messageLabel.setFont(new Font("Arial", Font.ITALIC, 14));
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            messageLabel.setForeground(new Color(100, 100, 100));
            
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setBackground(Color.WHITE);
            emptyPanel.add(messageLabel, BorderLayout.CENTER);
            
            // Remplacer la vue du tableau par le message
            JScrollPane scrollPane = (JScrollPane) seancesTable.getParent().getParent();
            if (scrollPane != null) {
                scrollPane.setViewportView(emptyPanel);
            }
        }
    }
    
    /**
     * Ouvre l'interface pour ajouter une nouvelle séance
     */
    private void ajouterNouvelleSeance() {
        // Cette méthode devrait ouvrir l'interface AjouterSeanceFrame
        // avec le coursId pré-sélectionné
        
        JOptionPane.showMessageDialog(
            this,
            "Fonctionnalité à implémenter: ouvrir l'interface d'ajout de séance avec le cours pré-sélectionné.",
            "Information",
            JOptionPane.INFORMATION_MESSAGE
        );
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
        
        SwingUtilities.invokeLater(() -> new VoirSeancesCoursFrame(1, "Programmation Orientée Objet"));
    }
}