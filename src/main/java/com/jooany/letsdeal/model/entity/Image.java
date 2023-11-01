package com.jooany.letsdeal.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "\"image\"")
@SequenceGenerator(
	name = "IMAGE_SEQ_GENERATOR",
	sequenceName = "IMAGE_SEQ",
	initialValue = 1, allocationSize = 50
)
public class Image {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "sale_id")
	private Sale sale;

	@Column(name = "imageUrl")
	private String imageUrl;

	@Column(name = "sortOrder")
	private Integer sortOrder;

	public static Image of(Sale sale, String imageUrl, Integer sortOrder) {
		return Image.builder()
			.sale(sale)
			.imageUrl(imageUrl)
			.sortOrder(sortOrder)
			.build();
	}

	public void updateSale(Sale sale) {
		this.sale = sale;
	}

}
