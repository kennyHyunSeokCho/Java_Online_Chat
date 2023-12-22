package Online_Chat.test;
import Online_Chat.main.MainHandler; 

//MainClient에서부터 받은 RID로 테이블 안에 들어가 해당 RID의 채팅을 시간 순서대로 excel출력

/*import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.sql.*; */

public class Chatlog {

  /*  private static final String URL = "jdbc:mysql://localhost:3306/talk_DB";
    private static final String USER = "root";
    private static final String PASSWORD = "Whgustjr92@@";
    private static final String DRIVER = "com.mysql.jdbc.Driver"; */

    public void exportChatLogToExcel(String rid) {
       /*  String sql = "SELECT * FROM chatlog WHERE RID = ? ORDER BY Timestamp";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, rid);
            ResultSet rs = pstmt.executeQuery();

            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Chat Logs");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("SenderID");
            headerRow.createCell(1).setCellValue("RID");
            headerRow.createCell(2).setCellValue("Message");
            headerRow.createCell(3).setCellValue("Timestamp");

            int rowNum = 1;
            while (rs.next()) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rs.getString("SenderID"));
                row.createCell(1).setCellValue(rs.getInt("RID"));
                row.createCell(2).setCellValue(rs.getString("Message"));
                row.createCell(3).setCellValue(rs.getTimestamp("Timestamp").toString());
            }

            try (FileOutputStream fileOut = new FileOutputStream("chat_logs.xlsx")) {
                workbook.write(fileOut);
            }

            workbook.close();
            System.out.println("Excel file has been generated!");

        } catch (Exception e) {
            e.printStackTrace();
        } */
    } 
}

