package com.example.Tugas1.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KelurahanModel {
	private long id;
	private String kode_kelurahan; 
	private long id_kecamatan;
	private KecamatanModel kecamatan;
	private String nama_kelurahan;
	private String kode_pos;
	private List<KeluargaModel> Keluarga;
}
