package com.jooany.letsdeal.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "message_groups")
@SequenceGenerator(
	name = "MESSAGE_GROUPS_SEQ_GENERATOR",
	sequenceName = "MESSAGE_GROUPS_SEQ"
)
public class MessageGroup extends SoftDeletableBaseTimeEntity{

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@ManyToOne(optional = false)
	@JoinColumn(name = "sale_id", nullable = false)
	private Sale sale;

	@ManyToOne(optional = false)
	@JoinColumn(name = "buyer_id", nullable = false)
	private User buyer;

	@ManyToOne
	@JoinColumn(name = "deleted_by")
	private User deletedBy;

}
