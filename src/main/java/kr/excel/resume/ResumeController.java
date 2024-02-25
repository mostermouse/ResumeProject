package kr.excel.resume;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.*;
import kr.excel.resume.PersonInfo;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;

public class ResumeController{
    private ResumeView view;
    private Workbook workbook;

    public ResumeController(){
        view = new ResumeView();
        workbook = new XSSFWorkbook();
    }

    public void createResume(){
        PersonInfo personInfo = view.inputPersonInfo();
        List<Education> educationList = view.inputEducationList();
        List<Career> careerList = view.inputCareerList();
        String selfIntroduction = view.inputSelfIntroduction();

        createResumeSheet(personInfo , educationList , careerList);
        createSelfIntroductionSheet(selfIntroduction);

        saveWorkbookToFile();
        System.out.println("履歴書生成が完了しました。");
    }

    private void createResumeSheet(PersonInfo personInfo , List<Education> educationList , List<Career> careerList){
        Sheet sheet = workbook.createSheet("履歴書");

        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("写真");
        headerRow.createCell(1).setCellValue("名前");
        headerRow.createCell(2).setCellValue("email");
        headerRow.createCell(3).setCellValue("住所");
        headerRow.createCell(4).setCellValue("電話番号");
        headerRow.createCell(5).setCellValue("生年月日");
        //데이터 삽입
        Row dataRow = sheet.createRow(1);
        String photoFilename = personInfo.getPhoto();
        try(InputStream photoStream = new FileInputStream(photoFilename)){
            //사진 파일을 읽어들입니다.
            BufferedImage originalImage = ImageIO.read(photoStream);

            //증명사진 크기로 이미지를 조절 (가로 35mm , 세로 45mm)
            int newWidth = (int)(35*2.83465);
            int newHeight = (int)(45*2.83465);
            Image resizedImage = originalImage.getScaledInstance(newWidth , newHeight , Image.SCALE_SMOOTH);
            BufferedImage resizedBufferedImage = new BufferedImage(newWidth , newHeight ,BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g2d = resizedBufferedImage.createGraphics();
            g2d.drawImage(resizedImage, 0 ,0, null);
            g2d.dispose();

            //조절된 이미지를 바이트 배열로 전환합니다.
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(resizedBufferedImage, "png",baos);
            byte[] imageBytes = baos.toByteArray();
            int imageIndex = workbook.addPicture(imageBytes , Workbook.PICTURE_TYPE_PNG);

            //Drawing 객체를 생성하고 이미지 삽입
            XSSFDrawing drawing = (XSSFDrawing) sheet.createDrawingPatriarch();
            XSSFClientAnchor anchor = new XSSFClientAnchor(0,0,0,0,0,1,1,2);
            drawing.createPicture(anchor,imageIndex);

            dataRow.setHeightInPoints(newHeight*72/96);
            int columnWidth = (int) (Math.floor((float)newWidth / (float) 8) * 256);
            sheet.setColumnWidth(0,columnWidth);


        }catch (IOException e){
            e.printStackTrace();
        }
        dataRow.createCell(1).setCellValue(personInfo.getName());
        dataRow.createCell(2).setCellValue(personInfo.getEmail());
        dataRow.createCell(3).setCellValue(personInfo.getAddress());
        dataRow.createCell(4).setCellValue(personInfo.getPhoneNumber());
        dataRow.createCell(5).setCellValue(personInfo.getBirthDate());

        int educationStarRow = 3;
        Row educationHeaderRow = sheet.createRow(educationStarRow -1);
        educationHeaderRow.createCell(0).setCellValue("卒業年");
        educationHeaderRow.createCell(1).setCellValue("学校名");
        educationHeaderRow.createCell(2).setCellValue("専攻");
        educationHeaderRow.createCell(3).setCellValue("卒業可否");

        int educationRowNum = educationHeaderRow.getRowNum();
        for(Education education : educationList){
            Row educationDataRow = sheet.createRow(educationRowNum++);
            educationDataRow.createCell(0).setCellValue(education.getGraduationStatus());
            educationDataRow.createCell(1).setCellValue(education.getSchoolName());
            educationDataRow.createCell(2).setCellValue(education.getMajor());
            educationDataRow.createCell(3).setCellValue(education.getGraduationStatus());
        }

        int careerStartRow = educationRowNum + 1;
        Row careerHeaderRow = sheet.createRow(careerStartRow -1);
        careerHeaderRow.createCell(0).setCellValue("勤務期間");
        careerHeaderRow.createCell(0).setCellValue("勤務所");
        careerHeaderRow.createCell(0).setCellValue("担当業務");
        careerHeaderRow.createCell(0).setCellValue("勤続年数");

        int careerRowNum = careerStartRow;
        for(Career career : careerList){
            Row careerDataRow = sheet.createRow(careerRowNum++);
            careerDataRow.createCell(0).setCellValue(career.getWorkPeriod());
            careerDataRow.createCell(1).setCellValue(career.getCompanyName());
            careerDataRow.createCell(2).setCellValue(career.getJobTitle());
            careerDataRow.createCell(3).setCellValue(career.getEmploymentYears());
        }
    }
    private void createSelfIntroductionSheet(String selfIntroduction){
        Sheet sheet = workbook.createSheet("自己紹介書");

        Row dataRow = sheet.createRow(0);
        Cell selfIntroductionCell = dataRow.createCell(0);
        selfIntroductionCell.setCellStyle(getWrapCellStyle());
        selfIntroductionCell.setCellValue(new XSSFRichTextString(selfIntroduction.replaceAll("\n" , String.valueOf((char) 10 ))));
    }
    private XSSFCellStyle getWrapCellStyle(){
        XSSFCellStyle style = (XSSFCellStyle) workbook.createCellStyle();
        style.setWrapText(true);
        return style;
    }

    private void saveWorkbookToFile(){
        try(FileOutputStream fileOut = new FileOutputStream("履歴書.xlsx")){
            workbook.write(fileOut);
        }catch (IOException e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
         ResumeController controller = new ResumeController();
         controller.createResume();
    }
}
