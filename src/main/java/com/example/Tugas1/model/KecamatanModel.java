package com.example.Tugas1.model;

import java.util.List;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KecamatanModel {
	private long id;
	private String kode_kecamatan;
	private long id_kota;
	private String nama_kecamatan;
	private KotaModel kota;
	private List<KelurahanModel> kelurahan;
}
