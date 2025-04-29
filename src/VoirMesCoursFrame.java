package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

/**
 * Interface pour visualiser les cours d'un enseignant
 */
public class VoirMesCoursFrame extends JFrame {
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    
    // Composants de l'interface
    private JTable coursTable;
    private DefaultTableModel tableModel;
    private JButton voirSeancesButton;
    private JButton fermerButton;
    
    // ID de l'enseignant
    private int enseignantId;

    public VoirMesCoursFrame(int enseignantId) {
        this.enseignantId = enseignantId;
        
        // Configuration de la fenêtre
        setTitle("Mes Cours");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
        
        // ====== ENTÊTE ======
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // ====== TABLEAU DES COURS ======
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.CENTER);
        
        // ====== PANEL DES BOUTONS ======
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Charger les cours
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
        
        JLabel titleLabel = new JLabel("MES COURS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crée le panneau du tableau des cours
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // En-tête du tableau
        String[] columns = {"ID", "Nom du cours", "Classe"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;  // Tableau non éditable
            }
        };
        
        // Création du tableau
        coursTable = new JTable(tableModel);
        coursTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        coursTable.setRowHeight(30);
        coursTable.setFont(new Font("Arial", Font.PLAIN, 14));
        coursTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        coursTable.getTableHeader().setBackground(new Color(220, 220, 220));
        coursTable.getTableHeader().setForeground(new Color(50, 50, 50));
        
        // Masquer la colonne ID
        coursTable.getColumnModel().getColumn(0).setMinWidth(0);
        coursTable.getColumnModel().getColumn(0).setMaxWidth(0);
        coursTable.getColumnModel().getColumn(0).setWidth(0);
        
        // Ajuster les largeurs des colonnes
        coursTable.getColumnModel().getColumn(1).setPreferredWidth(300);
        coursTable.getColumnModel().getColumn(2).setPreferredWidth(200);
        
        // Ajouter le tableau à un ScrollPane
        JScrollPane scrollPane = new JScrollPane(coursTable);
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
        
        // Bouton Voir les séances
        voirSeancesButton = new JButton("Voir les séances");
        voirSeancesButton.setFont(new Font("Arial", Font.BOLD, 14));
        voirSeancesButton.setForeground(Color.WHITE);
        voirSeancesButton.setBackground(primaryColor);
        voirSeancesButton.setPreferredSize(new Dimension(150, 40));
        voirSeancesButton.setFocusPainted(false);
        voirSeancesButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Bouton Fermer
        fermerButton = new JButton("Fermer");
        fermerButton.setFont(new Font("Arial", Font.BOLD, 14));
        fermerButton.setPreferredSize(new Dimension(150, 40));
        fermerButton.setFocusPainted(false);
        fermerButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Ajouter les boutons au panel
        buttonPanel.add(voirSeancesButton);
        buttonPanel.add(fermerButton);
        
        // Actions des boutons
        voirSeancesButton.addActionListener(e -> voirSeancesDuCours());
        fermerButton.addActionListener(e -> dispose());
        
        // Effet de survol
        voirSeancesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                voirSeancesButton.setBackground(new Color(0, 90, 180));
            }
            
            public void mouseExited(java.awt.event.MouseEvent evt) {
                voirSeancesButton.setBackground(primaryColor);
            }
        });
        
        return buttonPanel;
    }
    
    /**
     * Charge les cours de l'enseignant dans le tableau
     */
    private void chargerCours() {
        // Nettoyer le tableau
        tableModel.setRowCount(0);
        
        // Récupérer les cours de l'enseignant
        ArrayList<Map<String, Object>> cours = Donnees.getCoursEnseignant(enseignantId);
        
        // Remplir le tableau avec les cours
        for (Map<String, Object> unCours : cours) {
            Object[] row = {
                unCours.get("id"),
                unCours.get("nom"),
                unCours.get("classe_nom")
            };
            tableModel.addRow(row);
        }
        
        // Désactiver le bouton si aucun cours n'est disponible
        voirSeancesButton.setEnabled(tableModel.getRowCount() > 0);
    }
    
    /**
     * Affiche les séances du cours sélectionné
     */
    private void voirSeancesDuCours() {
        int selectedRow = coursTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                this,
                "Veuillez sélectionner un cours",
                "Information",
                JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }
        
        // Récupérer l'ID et le nom du cours sélectionné
        int coursId = (int) tableModel.getValueAt(selectedRow, 0);
        String coursNom = (String) tableModel.getValueAt(selectedRow, 1);
        
        // Ouvrir la fenêtre des séances
        new VoirSeancesCoursFrame(coursId, coursNom);
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
        
        SwingUtilities.invokeLater(() -> new VoirMesCoursFrame(2));
    }
}