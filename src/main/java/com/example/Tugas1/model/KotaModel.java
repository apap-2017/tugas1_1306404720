package com.example.Tugas1.model;

import java.util.List;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KotaModel {
	private long id;
	private String kode_kota;
	private String nama_kota;
	private List<KecamatanModel> kecamatan;
}
