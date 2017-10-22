package com.example.Tugas1.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeluargaModel {
	private long id;
	private String nomor_kk;
	private String alamat;
	private String rt;
	private String rw;
	private long id_kelurahan;
	private KelurahanModel kelurahan;
	private int is_tidak_berlaku;
	private List<PendudukModel> penduduk;
}
