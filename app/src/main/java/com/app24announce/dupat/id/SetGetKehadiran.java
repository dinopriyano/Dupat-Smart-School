package com.app24announce.dupat.id;

public class SetGetKehadiran {
    private String kelas,keterangan,nama,nis;

    public SetGetKehadiran() {
    }

    public SetGetKehadiran(String kelas, String keterangan, String nama, String nis) {
        this.kelas = kelas;
        this.keterangan = keterangan;
        this.nama = nama;
        this.nis = nis;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

}

