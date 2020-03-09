package com.app24announce.dupat.id;

public class SetGetDetailTugasPage {

    String namafile,url;
    long ukuranfile;

    public SetGetDetailTugasPage() {
    }

    public SetGetDetailTugasPage(String namafile, String url, long ukuranfile) {
        this.namafile = namafile;
        this.url = url;
        this.ukuranfile = ukuranfile;
    }

    public String getNamafile() {
        return namafile;
    }

    public void setNamafile(String namafile) {
        this.namafile = namafile;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getUkuranfile() {
        return ukuranfile;
    }

    public void setUkuranfile(long ukuranfile) {
        this.ukuranfile = ukuranfile;
    }
}
