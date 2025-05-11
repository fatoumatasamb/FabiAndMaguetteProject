package src;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.sql.*;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.PageSize;


/**
 * Interface pour la génération de fiches pédagogiques
 */
public class FichePedagogiqueFrame extends JFrame {
    // Couleurs de l'application
    private Color primaryColor = new Color(0, 102, 204);    // Bleu principal
    private Color backgroundColor = new Color(240, 240, 240); // Gris clair pour le fond
    private Color accentColor = new Color(255, 153, 0);     // Orange pour accentuation

    // Composants de l'interface
    private JComboBox<String> comboFiltre;
    private JComboBox<String> comboEnseignants;
    private JComboBox<String> comboCours;
    private JTable tableSeances;
    private DefaultTableModel tableModel;
    private JTextField txtDateDebut;
    private JTextField txtDateFin;
    private JButton btnGenererPDF;
    private JButton btnApercu;
    
    // Maps pour stocker les données
    private Map<String, Integer> enseignantsMap = new HashMap<>();
    private Map<String, Integer> coursMap = new HashMap<>();

    public FichePedagogiqueFrame() {
        // Configuration de la fenêtre
        setTitle("Génération de Fiche Pédagogique");
        setSize(750, 800);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Layout principal
        setLayout(new BorderLayout());
        getContentPane().setBackground(backgroundColor);
        
        // Création des panneaux
        JPanel headerPanel = createHeaderPanel();
        JPanel mainPanel = createMainPanel();
        JPanel footerPanel = createFooterPanel();
        
        // Ajout des panneaux à la fenêtre
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);
        
        // Chargement initial des données
        chargerEnseignants();
        comboFiltre.setSelectedIndex(0);
        
        // Affichage de la fenêtre
        setVisible(true);
    }
    
    /**
     * Crée le panneau d'entête
     */
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel titleLabel = new JLabel("GÉNÉRATION DE FICHE PÉDAGOGIQUE");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        return headerPanel;
    }
    
    /**
     * Crée le panneau principal
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        
        // Panneau des filtres
        JPanel filtrePanel = new JPanel(new GridBagLayout());
        filtrePanel.setBackground(Color.WHITE);
        filtrePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryColor), 
                "Filtres", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14), 
                primaryColor));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Type de filtre (par enseignant ou par cours)
        JLabel lblFiltre = new JLabel("Filtrer par:");
        lblFiltre.setFont(new Font("Arial", Font.BOLD, 14));
        filtrePanel.add(lblFiltre, gbc);
        
        gbc.gridx = 1;
        String[] filtreOptions = {"Enseignant", "Cours"};
        comboFiltre = new JComboBox<>(filtreOptions);
        comboFiltre.setPreferredSize(new Dimension(200, 30));
        filtrePanel.add(comboFiltre, gbc);
        
        // Enseignants
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblEnseignant = new JLabel("Enseignant:");
        lblEnseignant.setFont(new Font("Arial", Font.BOLD, 14));
        filtrePanel.add(lblEnseignant, gbc);
        
        gbc.gridx = 1;
        comboEnseignants = new JComboBox<>();
        comboEnseignants.setPreferredSize(new Dimension(200, 30));
        filtrePanel.add(comboEnseignants, gbc);
        
        // Cours
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblCours = new JLabel("Cours:");
        lblCours.setFont(new Font("Arial", Font.BOLD, 14));
        filtrePanel.add(lblCours, gbc);
        
        gbc.gridx = 1;
        comboCours = new JComboBox<>();
        comboCours.setPreferredSize(new Dimension(200, 30));
        filtrePanel.add(comboCours, gbc);
        
        // Période (optionnel)
        gbc.gridx = 0;
        gbc.gridy = 3;
        JLabel lblPeriode = new JLabel("Période (optionnel):");
        lblPeriode.setFont(new Font("Arial", Font.BOLD, 14));
        filtrePanel.add(lblPeriode, gbc);
        
        gbc.gridx = 1;
        JPanel periodePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        periodePanel.setBackground(Color.WHITE);
        
        txtDateDebut = new JTextField(10);
        txtDateFin = new JTextField(10);
        JLabel lblA = new JLabel("à");
        
        periodePanel.add(txtDateDebut);
        periodePanel.add(lblA);
        periodePanel.add(txtDateFin);
        
        filtrePanel.add(periodePanel, gbc);
        
        // Bouton Aperçu
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        btnApercu = new JButton("Afficher l'aperçu");
        btnApercu.setBackground(primaryColor);
        btnApercu.setForeground(Color.WHITE);
        btnApercu.setFont(new Font("Arial", Font.BOLD, 14));
        btnApercu.setFocusPainted(false);
        filtrePanel.add(btnApercu, gbc);
        
        // Panneau de l'aperçu (tableau des séances)
        JPanel apercuPanel = new JPanel(new BorderLayout());
        apercuPanel.setBackground(Color.WHITE);
        apercuPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(primaryColor), 
                "Aperçu des séances", 
                TitledBorder.LEFT, 
                TitledBorder.TOP, 
                new Font("Arial", Font.BOLD, 14), 
                primaryColor));
        
        // Modèle de tableau
        String[] colonnes = {"Date", "Contenu", "Validée"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        tableSeances = new JTable(tableModel);
        tableSeances.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        tableSeances.getTableHeader().setBackground(primaryColor);
        tableSeances.getTableHeader().setForeground(Color.WHITE);
        tableSeances.setFont(new Font("Arial", Font.PLAIN, 14));
        tableSeances.setRowHeight(25);
        
        JScrollPane scrollPane = new JScrollPane(tableSeances);
        apercuPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Organisation du panneau principal
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, filtrePanel, apercuPanel);
        splitPane.setDividerLocation(200);
        splitPane.setDividerSize(10);
        mainPanel.add(splitPane, BorderLayout.CENTER);
        
        // Ajout des écouteurs d'événements
        comboFiltre.addActionListener(e -> {
            if (comboFiltre.getSelectedIndex() == 0) { // Enseignant
                comboEnseignants.setEnabled(true);
                if (comboEnseignants.getSelectedItem() != null) {
                    chargerCoursPourEnseignant((String) comboEnseignants.getSelectedItem());
                }
            } else { // Cours
                comboEnseignants.setEnabled(false);
                chargerTousLesCours();
            }
        });
        
        comboEnseignants.addActionListener(e -> {
            if (comboEnseignants.getSelectedItem() != null) {
                chargerCoursPourEnseignant((String) comboEnseignants.getSelectedItem());
            }
        });
        
        btnApercu.addActionListener(e -> afficherApercu());
        
        return mainPanel;
    }
    
    /**
     * Crée le panneau de pied de page avec le bouton de génération
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(backgroundColor);
        footerPanel.setBorder(new EmptyBorder(10, 20, 10, 20));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(backgroundColor);
        
        btnGenererPDF = new JButton("Générer PDF");
        btnGenererPDF.setBackground(accentColor);
        btnGenererPDF.setForeground(Color.WHITE);
        btnGenererPDF.setFont(new Font("Arial", Font.BOLD, 14));
        btnGenererPDF.setPreferredSize(new Dimension(150, 40));
        btnGenererPDF.setFocusPainted(false);
        
        JButton btnFermer = new JButton("Fermer");
        btnFermer.setBackground(new Color(220, 53, 69));
        btnFermer.setForeground(Color.WHITE);
        btnFermer.setFont(new Font("Arial", Font.BOLD, 14));
        btnFermer.setPreferredSize(new Dimension(150, 40));
        btnFermer.setFocusPainted(false);
        
        buttonPanel.add(btnFermer);
        buttonPanel.add(btnGenererPDF);
        
        footerPanel.add(buttonPanel, BorderLayout.EAST);
        
        // Ajout des écouteurs d'événements
        btnGenererPDF.addActionListener(e -> genererPDF());
        btnFermer.addActionListener(e -> dispose());
        
        return footerPanel;
    }
    
    /**
     * Charge la liste des enseignants depuis la base de données
     */
    private void chargerEnseignants() {
        enseignantsMap.clear();
        comboEnseignants.removeAllItems();
        
        ArrayList<Map<String, Object>> enseignants = Donnees.getEnseignants();
        
        for (Map<String, Object> enseignant : enseignants) {
            String nom = (String) enseignant.get("nom");
            int id = (int) enseignant.get("id");
            enseignantsMap.put(nom, id);
            comboEnseignants.addItem(nom);
        }
        
        // Charger les cours pour le premier enseignant s'il existe
        if (comboEnseignants.getItemCount() > 0) {
            chargerCoursPourEnseignant((String) comboEnseignants.getItemAt(0));
        }
    }
    
    /**
     * Charge les cours pour un enseignant spécifique
     */
    private void chargerCoursPourEnseignant(String enseignantNom) {
        coursMap.clear();
        comboCours.removeAllItems();
        
        // Trouver l'ID de l'enseignant
        if (enseignantsMap.containsKey(enseignantNom)) {
            int enseignantId = enseignantsMap.get(enseignantNom);
            
            // Charger les cours de cet enseignant
            ArrayList<Map<String, Object>> cours = Donnees.getCoursEnseignant(enseignantId);
            
            for (Map<String, Object> unCours : cours) {
                String nomCours = (String) unCours.get("nom") + " (" + unCours.get("classe_nom") + ")";
                int idCours = (int) unCours.get("id");
                coursMap.put(nomCours, idCours);
                comboCours.addItem(nomCours);
            }
        }
    }
    
    /**
     * Charge tous les cours disponibles
     */
    private void chargerTousLesCours() {
        coursMap.clear();
        comboCours.removeAllItems();
        
        // Cette méthode n'existe pas encore dans Donnees.java
        // Vous devez l'implémenter ou l'adapter à votre structure
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = Donnees.connexion_a_la_base();
            stmt = conn.createStatement();
            String sql = "SELECT c.id, c.nom, cl.nom as classe_nom, u.nom as enseignant_nom " +
                         "FROM cours c JOIN classes cl ON c.classe_id = cl.id " +
                         "JOIN users u ON c.enseignant_id = u.id " +
                         "ORDER BY c.nom";
            rs = stmt.executeQuery(sql);
            
            while (rs.next()) {
                String nomCours = rs.getString("nom") + " (" + rs.getString("classe_nom") + ")";
                int idCours = rs.getInt("id");
                coursMap.put(nomCours, idCours);
                comboCours.addItem(nomCours);
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des cours: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Donnees.fermerResultSet(rs);
            Donnees.fermerStatement(stmt);
            Donnees.fermerConnexion(conn);
        }
    }
    
    /**
     * Affiche l'aperçu des séances dans le tableau
     */
    private void afficherApercu() {
        // Effacer le tableau
        tableModel.setRowCount(0);
        
        // Vérifier qu'un cours est sélectionné
        if (comboCours.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cours", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Récupérer l'ID du cours sélectionné
        String coursNom = (String) comboCours.getSelectedItem();
        int coursId = coursMap.get(coursNom);
        
        // Récupérer les séances pour ce cours
        ArrayList<Map<String, Object>> seances = Donnees.getSeancesCours(coursId);
        
        // Remplir le tableau avec les séances
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Map<String, Object> seance : seances) {
            java.util.Date date = (java.util.Date) seance.get("date");
            String contenu = (String) seance.get("contenu");
            boolean validee = (boolean) seance.get("validee");
            
            tableModel.addRow(new Object[]{
                sdf.format(date),
                contenu,
                validee ? "Oui" : "Non"
            });
        }
        
        if (seances.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Aucune séance trouvée pour ce cours", 
                "Information", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Génère le PDF de la fiche pédagogique
     */
    private void genererPDF() {
        // Vérifier qu'un cours est sélectionné
        if (comboCours.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un cours", "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Récupérer l'ID du cours sélectionné
        String coursNom = (String) comboCours.getSelectedItem();
        int coursId = coursMap.get(coursNom);
        
        // Récupérer les informations du cours
        Map<String, Object> infoCours = recupererInfoCours(coursId);
        if (infoCours == null) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération des informations du cours", 
                                         "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Récupérer les séances pour ce cours
        ArrayList<Map<String, Object>> seances = Donnees.getSeancesCours(coursId);
        
        // Demander où sauvegarder le fichier
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Enregistrer la fiche pédagogique");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setSelectedFile(new File("Fiche_Pedagogique_" + 
                                           infoCours.get("nom").toString().replaceAll("\\s+", "_") + 
                                           ".pdf"));
        
        if (fileChooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        
        File fichierPDF = fileChooser.getSelectedFile();
        if (!fichierPDF.getName().toLowerCase().endsWith(".pdf")) {
            fichierPDF = new File(fichierPDF.getAbsolutePath() + ".pdf");
        }
        
        try {
            // Créer le document PDF
            Document document = new Document(PageSize.A4);
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(fichierPDF));
            document.open();
            
            // Ajouter l'en-tête
            ajouterEntete(document);
            
            // Ajouter les informations du cours
            ajouterInfoCours(document, infoCours);
            
            // Ajouter le tableau des séances
            ajouterTableauSeances(document, seances);
            
            // Ajouter les statistiques
            ajouterStatistiques(document, seances);
            
            // Ajouter le pied de page
            ajouterPiedPage(document);
            
            // Fermer le document
            document.close();
            
            JOptionPane.showMessageDialog(this, 
                "Fiche pédagogique générée avec succès:\n" + fichierPDF.getAbsolutePath(), 
                "Succès", JOptionPane.INFORMATION_MESSAGE);
            
            // Ouvrir le fichier PDF
            try {
                Desktop.getDesktop().open(fichierPDF);
            } catch (Exception e) {
                System.out.println("Impossible d'ouvrir le PDF: " + e.getMessage());
            }
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Erreur lors de la génération du PDF: " + e.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    /**
     * Récupère les informations détaillées d'un cours
     */
    private Map<String, Object> recupererInfoCours(int coursId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Map<String, Object> infoCours = null;
        
        try {
            conn = Donnees.connexion_a_la_base();
            String sql = "SELECT c.nom, c.id, cl.nom as classe_nom, u.nom as enseignant_nom " +
                         "FROM cours c JOIN classes cl ON c.classe_id = cl.id " +
                         "JOIN users u ON c.enseignant_id = u.id " +
                         "WHERE c.id = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setInt(1, coursId);
            rs = stmt.executeQuery();
            
            if (rs.next()) {
                infoCours = new HashMap<>();
                infoCours.put("id", rs.getInt("id"));
                infoCours.put("nom", rs.getString("nom"));
                infoCours.put("classe_nom", rs.getString("classe_nom"));
                infoCours.put("enseignant_nom", rs.getString("enseignant_nom"));
            }
        } catch (SQLException e) {
            System.out.println("Erreur lors de la récupération des informations du cours: " + e.getMessage());
            e.printStackTrace();
        } finally {
            Donnees.fermerResultSet(rs);
            Donnees.fermerStatement(stmt);
            Donnees.fermerConnexion(conn);
        }
        
        return infoCours;
    }
    
    /**
     * Ajoute l'en-tête au document PDF
     */
    private void ajouterEntete(Document document) throws DocumentException {
        // Titre principal
        Paragraph titre = new Paragraph("UNIVERSITÉ IBA DER THIAM DE THIÈS", 
                                      new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD));
        titre.setAlignment(Element.ALIGN_CENTER);
        document.add(titre);
        
        // Sous-titre
        Paragraph sousTitre = new Paragraph("DÉPARTEMENT INFORMATIQUE", 
                                          new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14));
        sousTitre.setAlignment(Element.ALIGN_CENTER);
        document.add(sousTitre);
        
        // Titre du document
        Paragraph titreFiche = new Paragraph("FICHE DE SUIVI PÉDAGOGIQUE", 
                                           new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 16, com.itextpdf.text.Font.BOLD));
        titreFiche.setAlignment(Element.ALIGN_CENTER);
        titreFiche.setSpacingBefore(20);
        titreFiche.setSpacingAfter(20);
        document.add(titreFiche);
    }
    
    /**
     * Ajoute les informations du cours au document PDF
     */
    private void ajouterInfoCours(Document document, Map<String, Object> infoCours) throws DocumentException {
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(10);
        table.setSpacingAfter(10);
        
        // Style pour les en-têtes
        com.itextpdf.text.Font fontEntete = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD);
        com.itextpdf.text.Font fontTexte = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12);
        
        // Cours
        PdfPCell cellTitreCours = new PdfPCell(new Phrase("COURS:", fontEntete));
        cellTitreCours.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellTitreCours);
        
        PdfPCell cellCours = new PdfPCell(new Phrase((String) infoCours.get("nom"), fontTexte));
        cellCours.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellCours);
        
        // Enseignant
        PdfPCell cellTitreEnseignant = new PdfPCell(new Phrase("ENSEIGNANT:", fontEntete));
        cellTitreEnseignant.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellTitreEnseignant);
        
        PdfPCell cellEnseignant = new PdfPCell(new Phrase((String) infoCours.get("enseignant_nom"), fontTexte));
        cellEnseignant.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellEnseignant);
        
        // Classe
        PdfPCell cellTitreClasse = new PdfPCell(new Phrase("CLASSE:", fontEntete));
        cellTitreClasse.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellTitreClasse);
        
        PdfPCell cellClasse = new PdfPCell(new Phrase((String) infoCours.get("classe_nom"), fontTexte));
        cellClasse.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellClasse);
        
        // Année académique
        PdfPCell cellTitreAnnee = new PdfPCell(new Phrase("ANNÉE ACADÉMIQUE:", fontEntete));
        cellTitreAnnee.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellTitreAnnee);
        
        PdfPCell cellAnnee = new PdfPCell(new Phrase("2024-2025", fontTexte));
        cellAnnee.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellAnnee);
        
        document.add(table);
    }
    
    /**
     * Ajoute le tableau des séances au document PDF
     */
    private void ajouterTableauSeances(Document document, ArrayList<Map<String, Object>> seances) throws DocumentException {
        PdfPTable table = new PdfPTable(3);
        float[] columnWidths = {1, 4, 1};
        table.setWidths(columnWidths);
        table.setWidthPercentage(100);
        table.setSpacingBefore(20);
        table.setSpacingAfter(10);
        
        // Style pour les en-têtes et le contenu
        com.itextpdf.text.Font fontEntete = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12, com.itextpdf.text.Font.BOLD, BaseColor.WHITE);
        com.itextpdf.text.Font fontTexte = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
        
        // En-têtes du tableau
        PdfPCell cellDate = new PdfPCell(new Phrase("DATE", fontEntete));
        cellDate.setBackgroundColor(new BaseColor(0, 102, 204));
        cellDate.setPadding(5);
        cellDate.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellDate);
        
        PdfPCell cellContenu = new PdfPCell(new Phrase("CONTENU", fontEntete));
        cellContenu.setBackgroundColor(new BaseColor(0, 102, 204));
        cellContenu.setPadding(5);
        cellContenu.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellContenu);
        
        PdfPCell cellValidee = new PdfPCell(new Phrase("VALIDÉE", fontEntete));
        cellValidee.setBackgroundColor(new BaseColor(0, 102, 204));
        cellValidee.setPadding(5);
        cellValidee.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(cellValidee);
        
        // Ligne alternée pour chaque séance
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        BaseColor couleurLigneAlternee = new BaseColor(240, 240, 240);
        
        for (int i = 0; i < seances.size(); i++) {
            Map<String, Object> seance = seances.get(i);
            
            // Alternance des couleurs de ligne
            BaseColor couleurFond = (i % 2 == 0) ? couleurLigneAlternee : BaseColor.WHITE;
            
            // Date
            PdfPCell cellDateSeance = new PdfPCell(new Phrase(sdf.format((java.util.Date) seance.get("date")), fontTexte));
            cellDateSeance.setBackgroundColor(couleurFond);
            cellDateSeance.setPadding(5);
            table.addCell(cellDateSeance);
            
            // Contenu
            PdfPCell cellContenuSeance = new PdfPCell(new Phrase((String) seance.get("contenu"), fontTexte));
            cellContenuSeance.setBackgroundColor(couleurFond);
            cellContenuSeance.setPadding(5);
            table.addCell(cellContenuSeance);
            
            // Validée
            String valideeStr = (boolean) seance.get("validee") ? "Oui" : "Non";
            PdfPCell cellValideeSeance = new PdfPCell(new Phrase(valideeStr, fontTexte));
            cellValideeSeance.setBackgroundColor(couleurFond);
            cellValideeSeance.setPadding(5);
            cellValideeSeance.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cellValideeSeance);
        }
        
        document.add(table);
    }
    
    /**
     * Ajoute les statistiques au document PDF
     */
    private void ajouterStatistiques(Document document, ArrayList<Map<String, Object>> seances) throws DocumentException {
        // Calculer les statistiques
        int totalSeances = seances.size();
        int seancesValidees = 0;
        
        for (Map<String, Object> seance : seances) {
            if ((boolean) seance.get("validee")) {
                seancesValidees++;
            }
        }
        
        // Pourcentage de validation
        float pourcentage = (totalSeances > 0) ? ((float) seancesValidees / totalSeances) * 100 : 0;
        
        // Créer la section statistiques
        Paragraph titreStats = new Paragraph("STATISTIQUES:", 
                                           new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 14, com.itextpdf.text.Font.BOLD));
        titreStats.setSpacingBefore(20);
        titreStats.setSpacingAfter(10);
        document.add(titreStats);
        
        // Tableau pour les statistiques
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        
        com.itextpdf.text.Font fontTexte = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 12);
        
        // Nombre total de séances
        PdfPCell cellTitreTotal = new PdfPCell(new Phrase("Nombre total de séances:", fontTexte));
        cellTitreTotal.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellTitreTotal);
        
        PdfPCell cellTotal = new PdfPCell(new Phrase(String.valueOf(totalSeances), fontTexte));
        cellTotal.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellTotal);
        
        // Séances validées
        PdfPCell cellTitreValidees = new PdfPCell(new Phrase("Séances validées:", fontTexte));
        cellTitreValidees.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellTitreValidees);
        
        PdfPCell cellValidees = new PdfPCell(new Phrase(
            seancesValidees + " (" + String.format("%.1f", pourcentage) + "%)", fontTexte));
        cellValidees.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellValidees);
        
        document.add(table);
    }
    
    /**
     * Ajoute le pied de page au document PDF
     */
    private void ajouterPiedPage(Document document) throws DocumentException {
        // Date de génération
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dateGeneration = sdf.format(new java.util.Date());
        
        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(100);
        table.setSpacingBefore(30);
        
        com.itextpdf.text.Font fontTexte = new com.itextpdf.text.Font(com.itextpdf.text.Font.FontFamily.HELVETICA, 10);
        
        // Date de génération
        PdfPCell cellDate = new PdfPCell(new Phrase("Date de génération: " + dateGeneration, fontTexte));
        cellDate.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        table.addCell(cellDate);
        
        // Signature
        PdfPCell cellSignature = new PdfPCell(new Phrase("Signature du chef de département", fontTexte));
        cellSignature.setBorder(com.itextpdf.text.Rectangle.NO_BORDER);
        cellSignature.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(cellSignature);
        
        document.add(table);
        
        // Ligne pour la signature
        LineSeparator line = new LineSeparator();
        line.setOffset(-2);
        line.setLineWidth(0.5f);
        line.setPercentage(30);
        line.setAlignment(Element.ALIGN_RIGHT);
        
        Chunk lineChunk = new Chunk(line);
        Paragraph signatureLine = new Paragraph(lineChunk);
        signatureLine.setAlignment(Element.ALIGN_RIGHT);
        signatureLine.setSpacingBefore(-5);
        
        document.add(signatureLine);
    }
    
    /**
     * Point d'entrée pour tester la fenêtre
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
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
            
            new FichePedagogiqueFrame();
        });
    }
}