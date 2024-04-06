package com.example.wenhutils;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ReadExcelTask extends AsyncTask<File, Void, Void> {

    @Override
    protected Void doInBackground(File... files) {
        if (files.length == 0) {
            return null;
        }

        File excelFile = files[0];

        try {
            FileInputStream fis = new FileInputStream(excelFile);
            XSSFWorkbook workbook = new XSSFWorkbook(fis);

            // 获取第一个工作表
            XSSFSheet sheet = workbook.getSheetAt(0);

            // 迭代每一行
            for (Row row : sheet) {
                // 迭代每一列
                for (Cell cell : row) {
                    // 获取单元格内容并打印
                    Log.d("ExcelDemo", "Cell Value: " + cell.toString());
                }
            }

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
