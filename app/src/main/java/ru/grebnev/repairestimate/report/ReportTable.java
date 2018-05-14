package ru.grebnev.repairestimate.report;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.grebnev.repairestimate.data.firebase.database.FirebaseReadDatabase;
import ru.grebnev.repairestimate.models.ConceivableEmployment;
import ru.grebnev.repairestimate.models.Employment;
import ru.grebnev.repairestimate.models.EmploymentType;
import ru.grebnev.repairestimate.models.MaterialEmployment;
import ru.grebnev.repairestimate.models.Project;
import ru.grebnev.repairestimate.models.ServiceEmployment;

public class ReportTable {

    private static final String TAG = ReportTable.class.getSimpleName();

    public static final String FONT = "/res/font/russian.ttf";

    private String[] query = {"type", "list", "material", "service"};

    private FirebaseReadDatabase readDatabase;

    private Activity activity;

    private Project project;

    private List<Employment> employments;

    private static List<EmploymentType> types = new ArrayList<>();

    private List<ConceivableEmployment> conceivableEmployments;

    private List<MaterialEmployment> materialEmployments;

    private List<ServiceEmployment> serviceEmployments;

    private Document document;

    public ReportTable(Activity activity, Project project, List<Employment> employments, FirebaseReadDatabase readDatabase) {
        this.activity = activity;
        this.employments = employments;
        this.project = project;
        this.readDatabase = readDatabase;
    }

    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean checkPermission() {
        boolean isGrantedWriteStorage =
                ContextCompat.checkSelfPermission(activity.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED;
        if (!isGrantedWriteStorage) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1001);
            return false;
        }
        return true;
    }


    public void createPdfFile() {
        boolean externalStorageWritable = isExternalStorageWritable();
        if (!externalStorageWritable) {
            Log.d(TAG, "External storage is not available for write");
        } else {
            Log.d(TAG, "External storage is available for write");
        }

        document = new Document(PageSize.LETTER);

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "PdfFile");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.d(TAG, "Pdf Directory created");
        } else {
            Log.d(TAG, "Pdf Directory not created");
        }

        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        File myFile = new File(pdfFolder + timeStamp + ".pdf");

        try {
            PdfWriter.getInstance(document, new FileOutputStream(myFile));
            document.open();

            BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);

            headline(bf);

            document.add(new Paragraph("\n"));

            headlineTable(bf);

            Font font = new Font(bf,12, Font.NORMAL);

            types = readDatabase.getEmploymentTypes();

            for (EmploymentType type : types) {
                List<Employment> employmentTmp = new ArrayList<>();
                for (Employment employment : employments) {
                    conceivableEmployments = readDatabase.getConceivableEmployments();
                    for (ConceivableEmployment conceivable : conceivableEmployments) {
                        if (employment.getName().equals(conceivable.getName()) && conceivable.getType().equals(type.getName())) {
                            employmentTmp.add(employment);
                        }
                    }
                }

                headlineType(employmentTmp, type, font);

                PdfPTable table = new PdfPTable(5);
                table.setWidths(new float[] {3, 1, 1, 1, 2});

                for (Employment employment : employmentTmp) {
                    lineEmployment(table, employment, font);
                    linesMaterial(table, employment, font);
                    linesService(table, employment, font);
                }
                document.add(table);
            }

            PdfPTable table = new PdfPTable(2);
            table.setWidths(new float[] {6, 2});

            PdfPCell c1 = new PdfPCell(new Phrase("Итого:", font));
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
            table.addCell(c1);
            float sum = 0.0f;
            for (Employment employment : employments) {
                sum += employment.getCost();
            }
            c1 = new PdfPCell(new Phrase(String.valueOf(sum), font));
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c1);
            document.add(table);

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void headline(BaseFont baseFont) throws DocumentException {
        Font font = new Font(baseFont,18, Font.BOLD);
        Paragraph name = new Paragraph("Сметная стоимость: \"" + project.getNameProject() + "\"", font);
        name.setAlignment(Element.ALIGN_CENTER);
        document.add(name);
        font = new Font(baseFont,14);
        Paragraph date = new Paragraph("Дата создания: " + new SimpleDateFormat("dd.MM.yyyy").format(new Date(project.getDateProject())), font);
        date.setAlignment(Element.ALIGN_CENTER);
        document.add(date);
    }

    private void headlineTable(BaseFont baseFont) throws DocumentException {
        Font font = new Font(baseFont,12, Font.NORMAL);

        PdfPTable table = new PdfPTable(5);
        table.setWidths(new float[] {3, 1, 1, 1, 2});

        PdfPCell c1 = new PdfPCell(new Phrase("Наименование", font));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Ед. изм.", font));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Кол-во", font));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Цена", font));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        c1 = new PdfPCell(new Phrase("Стоимость", font));
        c1.setHorizontalAlignment(Element.ALIGN_LEFT);
        table.addCell(c1);

        table.setPaddingTop(3);

        document.add(table);
    }

    private void headlineType(List<Employment> employmentTmp, EmploymentType type, Font font) throws DocumentException {
        if (employmentTmp.size() > 0) {
            PdfPTable table = new PdfPTable(1);

            PdfPCell c1 = new PdfPCell(new Phrase(type.getName(), font));
            c1.setHorizontalAlignment(Element.ALIGN_CENTER);
            c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
            table.addCell(c1);
            document.add(table);
        }
    }

    private void lineEmployment(PdfPTable table, Employment employment, Font font) {
        PdfPCell c1 = new PdfPCell(new Phrase(employment.getName(), font));
        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("кв.м", font));
        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(c1);
        float volume = employment.getVolumes().get(0);
        for (int i = 1; i < employment.getVolumes().size(); i++) {
            volume = volume * employment.getVolumes().get(i);
        }
        c1 = new PdfPCell(Phrase.getInstance(String.valueOf(volume)));
        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(c1);
        c1 = new PdfPCell(new Phrase("-", font));
        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(c1);
        c1 = new PdfPCell(Phrase.getInstance(String.valueOf(employment.getCost())));
        c1.setBackgroundColor(BaseColor.LIGHT_GRAY);
        table.addCell(c1);
    }

    private void linesMaterial(PdfPTable table, Employment employment, Font font) {
        materialEmployments = readDatabase.getMaterialEmployments();
        for (String materialEmployment : employment.getMaterials()) {
            for (MaterialEmployment material : materialEmployments) {
                if (material.getName().equals(materialEmployment) && material.getEmployment().equals(employment.getName())) {
                    PdfPCell c1 = new PdfPCell(new Phrase(material.getName(), font));
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(material.getUnit(), font));
                    table.addCell(c1);
                    float volume = employment.getVolumes().get(0) * material.getVolumesOfUnit().get(0);
                    for (int i = 1; i < employment.getVolumes().size(); i++) {
                        volume = volume * (employment.getVolumes().get(i) * material.getVolumesOfUnit().get(i));
                    }
                    c1 = new PdfPCell(Phrase.getInstance(String.valueOf(Math.ceil(volume))));
                    table.addCell(c1);
                    c1 = new PdfPCell(Phrase.getInstance(String.valueOf(material.getPrice())));
                    table.addCell(c1);
                    c1 = new PdfPCell(Phrase.getInstance(String.valueOf(Math.ceil(volume)*material.getPrice())));
                    table.addCell(c1);
                }
            }
        }
    }

    private void linesService(PdfPTable table, Employment employment, Font font) {
        serviceEmployments = readDatabase.getServiceEmployments();
        for (String serviceEmployment : employment.getServices()) {
            for (ServiceEmployment service : serviceEmployments) {
                if (service.getName().equals(serviceEmployment)) {
                    PdfPCell c1 = new PdfPCell(new Phrase(service.getName(), font));
                    table.addCell(c1);
                    c1 = new PdfPCell(new Phrase(service.getUnit(), font));
                    table.addCell(c1);
                    float volume = employment.getVolumes().get(0);
                    for (int i = 1; i < employment.getVolumes().size(); i++) {
                        volume = volume * employment.getVolumes().get(i);
                    }
                    c1 = new PdfPCell(Phrase.getInstance(String.valueOf(volume*service.getVolumeOfUnit())));
                    table.addCell(c1);
                    c1 = new PdfPCell(Phrase.getInstance(String.valueOf(service.getPrice())));
                    table.addCell(c1);
                    c1 = new PdfPCell(Phrase.getInstance(String.valueOf(Math.ceil(volume*service.getVolumeOfUnit())*service.getPrice())));
                    table.addCell(c1);
                }
            }
        }
    }
}
