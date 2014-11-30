package main;

import utils.CIME2CSV;
public class Runner {
    public static void main(String[] args) {
        String files_dir = "src/files/";
        CIME2CSV cime = new CIME2CSV(files_dir+ "test.CIME", "Province", files_dir);
        cime.splitCIME2CSV();
        cime.close();

    }
}
