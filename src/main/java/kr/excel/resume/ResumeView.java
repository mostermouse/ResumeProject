package kr.excel.resume;

import javax.swing.plaf.IconUIResource;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResumeView {
    private Scanner sc;
    public ResumeView(){
        sc = new Scanner(System.in);
    }
    public PersonInfo inputPersonInfo(){
        System.out.print("写真のファイル名を入力してください:");
        String photo = sc.nextLine();

        System.out.print("名前を入力してください:");
        String name = sc.nextLine();

        System.out.print("emailを入力してください:");
        String email = sc.nextLine();

        System.out.println("住所を入力してください:");
        String address = sc.nextLine();

        System.out.print("電話番号を入力してください:");
        String phoneNumber =sc.nextLine();

        System.out.print("生年月日を入力してください (例 : 1998-01-01):");
        String birthDate = sc.nextLine();
        return new PersonInfo(photo , name , email ,address, phoneNumber , birthDate);
    }
    public List<Education> inputEducationList(){
        List<Education> educationList = new ArrayList<>();

        while(true){
            System.out.println("学歴の情報を入力してください（終了は q）:");
            System.out.println("卒業年 学校名 専攻 卒業可否");

            String input = sc.nextLine();
            if(input.equalsIgnoreCase("q")){
                break;
            }
            String[] tokens = input.split(" ");
            if(tokens.length != 4){
                System.out.println("入力が間違えました");
                continue;
            }
            String graduationYear = tokens[0];
            String schoolName = tokens[1];
            String major = tokens[2];
            String graduationStatus = tokens[3];

            educationList.add(new Education(graduationYear , schoolName , major , graduationStatus));
        }
        return educationList;
    }
    public List<Career> inputCareerList(){
        List<Career> careerList = new ArrayList<>();

        while(true){
            System.out.println("経歴情報を入力してください（終了は　q）：");
            System.out.println("勤務期間　勤務所　担当業務　勤続年数");

            String input = sc.nextLine();
            if(input.equalsIgnoreCase("q")){
                break;
            }
            String[] tokens = input.split(" ");
            if(tokens.length != 4){
                System.out.println("入力が間違えました");
                continue;
            }
            String workPeriod = tokens[0];
            String workplace =tokens[1];
            String duties = tokens[2];
            String yearsEmployed = tokens[3];

            careerList.add(new Career(workPeriod, workplace , duties , yearsEmployed));
        }
        return careerList;
    }
    public String inputSelfIntroduction(){
        System.out.println("自己紹介書を入力してください。　数列にした場合には空き列に入力してください。");
        StringBuilder sb = new StringBuilder();
        String line;
        while((line = sc.nextLine()).trim().length() > 0){
            sb.append(line).append("\n");
        }
        return sb.toString().trim();
    }
}
