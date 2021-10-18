package com.example.admin.cauteladematerial;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Toast;

import android.support.annotation.NonNull;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ExportarExcel extends Activity {
    private DBHelper dbHelper;
    private ArrayList<Items> itemsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemsList = new ArrayList<>();
        dbHelper = new DBHelper(ExportarExcel.this);

        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory", "com.fasterxml.aalto.stax.InputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory", "com.fasterxml.aalto.stax.OutputFactoryImpl");
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory", "com.fasterxml.aalto.stax.EventFactoryImpl");


        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            if (getApplicationContext().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                requestPermissions(permissions, 1);
            } else {
                importData();
            }
            } else {
                importData();
            }

    }

    private void importData() {
        itemsList= dbHelper.getAllLocalUser();

        if(itemsList.size()>0){
            createXlFile();
        } else {
            Toast.makeText(this, "Lista vazia\nPlanilha não gerada", Toast.LENGTH_SHORT).show();
            finish();
        }


    }

    private void createXlFile() {


        // File filePath = new File(Environment.getExternalStorageDirectory() + "/Demo.xls");
        Workbook wb = new HSSFWorkbook();


        Cell cell = null;

        Sheet sheet = null;
        sheet = wb.createSheet("Registro de Cautelas");
        //Now column and row
        Row row = sheet.createRow(0);

        cell = row.createCell(0);
        cell.setCellValue("Data/Hora");


        cell = row.createCell(1);
        cell.setCellValue("Ano");


        cell = row.createCell(2);
        cell.setCellValue("Mês");


        cell = row.createCell(3);
        cell.setCellValue("Destino");


        cell = row.createCell(4);
        cell.setCellValue("Tipo");


        cell = row.createCell(5);
        cell.setCellValue("Material");

        cell = row.createCell(6);
        cell.setCellValue("Quantia");

        cell = row.createCell(7);
        cell.setCellValue("Militar");

        cell = row.createCell(8);
        cell.setCellValue("Info");


        //column width
        sheet.setColumnWidth(0, (30 * 200));
        sheet.setColumnWidth(1, (15 * 200));
        sheet.setColumnWidth(2, (15 * 200));
        sheet.setColumnWidth(3, (30 * 200));
        sheet.setColumnWidth(4, (20 * 200));
        sheet.setColumnWidth(5, (50 * 200));
        sheet.setColumnWidth(6, (15 * 200));
        sheet.setColumnWidth(7, (50 * 200));
        sheet.setColumnWidth(8, (50 * 200));


        for (int i = 0; i < itemsList.size(); i++) {
            Row row1 = sheet.createRow(i + 1);

            cell = row1.createCell(0);
            cell.setCellValue(itemsList.get(i).getitemdata());

            cell = row1.createCell(1);
            cell.setCellValue((itemsList.get(i).getitemano()));
            //  cell.setCellStyle(cellStyle);

            cell = row1.createCell(2);
            cell.setCellValue(itemsList.get(i).getitemmes());

            cell = row1.createCell(3);
            cell.setCellValue(itemsList.get(i).getitemdestino());

            cell = row1.createCell(4);
            cell.setCellValue(itemsList.get(i).getitemtipo());

            cell = row1.createCell(5);
            cell.setCellValue(itemsList.get(i).getitemmaterial());

            cell = row1.createCell(6);
            cell.setCellValue(itemsList.get(i).getitemquantia());

            cell = row1.createCell(7);
            cell.setCellValue(itemsList.get(i).getitemmilitar());

            cell = row1.createCell(8);
            cell.setCellValue(itemsList.get(i).getiteminfo());


            sheet.setColumnWidth(0, (30 * 200));
            sheet.setColumnWidth(1, (15 * 200));
            sheet.setColumnWidth(2, (15 * 200));
            sheet.setColumnWidth(3, (30 * 200));
            sheet.setColumnWidth(4, (20 * 200));
            sheet.setColumnWidth(5, (50 * 200));
            sheet.setColumnWidth(6, (15 * 200));
            sheet.setColumnWidth(7, (50 * 200));
            sheet.setColumnWidth(8, (50 * 200));

        }
        String folderName = "Registro de Cautelas";
        String fileName = "Dados Brutos" + ".xls";
        String path = Environment.getExternalStorageDirectory() + File.separator + folderName + File.separator + fileName;

        File file = new File(Environment.getExternalStorageDirectory() + File.separator + folderName);
        if (!file.exists()) {
            file.mkdirs();
        }

        FileOutputStream outputStream = null;

        try {
            outputStream = new FileOutputStream(path);
            wb.write(outputStream);
            // ShareViaEmail(file.getParentFile().getName(),file.getName());
            Toast.makeText(getApplicationContext(), "Planilha gerada em\n" + path, Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            e.printStackTrace();

            Toast.makeText(getApplicationContext(), "Algo deu errado\nErro (x0002)", Toast.LENGTH_LONG).show();
            finish();
            try {
                outputStream.close();
                finish();
            } catch (Exception ex) {
                ex.printStackTrace();
                finish();
            }
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            importData();
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Permissão negada", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

}
