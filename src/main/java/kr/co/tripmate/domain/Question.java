package kr.co.tripmate.domain;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
@Table(name = "tbl_question")
public class Question {

	@Id
	@GeneratedValue(generator = "seq_question" ,strategy = GenerationType.IDENTITY)
	@JsonProperty
	private Long id;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_question_writer"))
	@JsonProperty
	private User writer;
	@JsonProperty
	private String title;
//	@Lob
	@JsonProperty
	private String contents;
	
	@CreationTimestamp
	private LocalDateTime regdate;
	
	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
	@OrderBy(value = "id ASC")
	private List<Answer> answers;

	public Question() {}
	
	public Question(User writer, String title, String contents) {
		this.writer = writer;
		this.title = title;
		this.contents = contents;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}
	
	public String getFormattedRegdate() {
		if ( regdate == null ) {
			return "";
		}
		
		return regdate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public void update(String title, String contents) {
		this.title = title;
		this.contents = contents;
	}
	
	public boolean isWriter(User sessionUser) {
		if ( sessionUser == null ) {
			return false;
		}
		
		return this.writer.equals(sessionUser);
	}

}
