package guru.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class FileParseJava {
    ClassLoader cl = FileParseJava.class.getClassLoader();

    @Test
    void zipTest() throws Exception {

        InputStream is = cl.getResourceAsStream("HobbyZip.zip");
        ZipFile zf = new ZipFile(new File("src/test/resources/HobbyZip.zip"));

        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry entry;
        while ((entry = zis.getNextEntry()) != null) {
            String entryName = entry.getName();
            long size = entry.getSize();
            System.out.println("Name: " + entryName + ", Size: " + size);

            switch (entryName) {
                case "VampireTheMasquerade_Revised_Ed_rus_1999.pdf":
                    System.out.println("- PDF");
                    try (InputStream stream_pdf = zf.getInputStream(entry)) {
                        PDF pdf = new PDF(stream_pdf);
                        assertThat(pdf.text).contains("Собрание Зверей");
                    }

                    break;
                case "Characters.xlsx":
                    System.out.println("- XLSX");
                    try (InputStream stream_xlsx = zf.getInputStream(entry)) {
                        XLS xls = new XLS(stream_xlsx);
                        assertThat(xls.excel.getSheetAt(0)
                                .getRow(1)
                                .getCell(0)
                                .getStringCellValue())
                                .isEqualTo("Julian");
                    }
                    break;

                case "Characters.csv":
                    System.out.println("- CSV");
                    try (InputStream stream_pdf = zf.getInputStream(entry)) {
                        CSVReader reader = new CSVReader(new InputStreamReader(is));
                        {

                            List<String[]> content = reader.readAll();
                            String[] row = content.get(2);
                            assertThat(row[0]).isEqualTo("Julian");
                            assertThat(row[1]).isEqualTo("Human");
                        }
                    }
                    break;


            }


        }
        }}




