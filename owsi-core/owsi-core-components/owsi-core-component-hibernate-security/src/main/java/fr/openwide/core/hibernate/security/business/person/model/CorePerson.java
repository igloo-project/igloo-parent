package fr.openwide.core.hibernate.security.business.person.model;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.OrderBy;
import javax.persistence.Transient;

import org.apache.solr.analysis.ASCIIFoldingFilterFactory;
import org.apache.solr.analysis.KeywordTokenizerFactory;
import org.apache.solr.analysis.LowerCaseFilterFactory;
import org.apache.solr.analysis.WhitespaceTokenizerFactory;
import org.apache.solr.analysis.WordDelimiterFilterFactory;
import org.bindgen.Bindable;
import org.hibernate.annotations.Cascade;
import org.hibernate.search.annotations.Analyzer;
import org.hibernate.search.annotations.AnalyzerDef;
import org.hibernate.search.annotations.AnalyzerDefs;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Parameter;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.annotations.TokenFilterDef;
import org.hibernate.search.annotations.TokenizerDef;

import fr.openwide.core.commons.util.CloneUtils;
import fr.openwide.core.hibernate.business.generic.model.GenericEntity;
import fr.openwide.core.hibernate.search.util.HibernateSearchAnalyzer;
import fr.openwide.core.hibernate.security.acl.domain.User;
import fr.openwide.core.hibernate.security.business.authority.model.Authority;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Indexed
@Bindable
@AnalyzerDefs({
	@AnalyzerDef(name = HibernateSearchAnalyzer.KEYWORD,
			tokenizer = @TokenizerDef(factory = KeywordTokenizerFactory.class)
	),
	@AnalyzerDef(name = HibernateSearchAnalyzer.TEXT,
			tokenizer = @TokenizerDef(factory = WhitespaceTokenizerFactory.class),
			filters = {
					@TokenFilterDef(factory = ASCIIFoldingFilterFactory.class),
					@TokenFilterDef(factory = WordDelimiterFilterFactory.class, params = {
									@Parameter(name = "generateWordParts", value = "1"),
									@Parameter(name = "generateNumberParts", value = "1"),
									@Parameter(name = "catenateWords", value = "0"),
									@Parameter(name = "catenateNumbers", value = "0"),
									@Parameter(name = "catenateAll", value = "0"),
									@Parameter(name = "splitOnCaseChange", value = "0"),
									@Parameter(name = "splitOnNumerics", value = "0"),
									@Parameter(name = "preserveOriginal", value = "1")
							}
					),
					@TokenFilterDef(factory = LowerCaseFilterFactory.class)
			}
	)
})
public class CorePerson extends GenericEntity<Integer, CorePerson>
		implements User {

	private static final long serialVersionUID = 1803671157183603979L;
	
	@Id
	@GeneratedValue
	@DocumentId
	private Integer id;
	
	@Field(index=Index.TOKENIZED, store=Store.NO)
	@Analyzer(definition = HibernateSearchAnalyzer.TEXT)
	private String userName;
	
	@Field(index=Index.TOKENIZED, store=Store.NO)
	@Analyzer(definition = HibernateSearchAnalyzer.TEXT)
	private String firstName;
	
	@Field(index=Index.TOKENIZED, store=Store.NO)
	@Analyzer(definition = HibernateSearchAnalyzer.TEXT)
	private String lastName;
	
	private String email;
	
	private String phoneNumber;
	
	private String gsmNumber;
	
	private String faxNumber;
	
	private String md5Password = "*NO PASSWORD*";
	
	private boolean active = true;
	
	@Column(nullable = false)
	private Date creationDate = new Date();
	
	@Column(nullable = false)
	private Date lastUpdateDate = new Date();
	
	private Date lastLoginDate;
	
	/**
	 * preferred locale for user, can be null
	 */
	private Locale locale;
	
	@ManyToMany
	@Cascade({org.hibernate.annotations.CascadeType.SAVE_UPDATE})
	@OrderBy("name")
	private Set<Authority> authorities = new LinkedHashSet<Authority>();
	
	@ManyToMany(mappedBy="persons")
	private List<CorePersonGroup> groups = new LinkedList<CorePersonGroup>();
	
	public CorePerson() {
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	@Transient
	public String getFullName() {
		StringBuilder builder = new StringBuilder();
		if(firstName != null) {
			builder.append(firstName);
			builder.append(" ");
		}
		if(lastName != null && !lastName.equals(firstName)) {
			builder.append(lastName);
		}
		return builder.toString().trim();
	}

	public Set<Authority> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<Authority> authorities) {
		this.authorities = authorities;
	}
	
	public void addAuthority(Authority authority) {
		this.authorities.add(authority);
	}
	
	public void removeAuthority(Authority authority) {
		this.authorities.remove(authority);
	}
	
	public List<CorePersonGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<CorePersonGroup> groups) {
		this.groups = groups;
	}

	@Override
	public String getNameForToString() {
		return getUserName();
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setGsmNumber(String gsmNumber) {
		this.gsmNumber = gsmNumber;
	}

	public String getGsmNumber() {
		return gsmNumber;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getMd5Password() {
		return md5Password;
	}

	public void setMd5Password(String md5Password) {
		this.md5Password = md5Password;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isActive() {
		return active;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = CloneUtils.clone(lastLoginDate);
	}

	public Date getLastLoginDate() {
		return CloneUtils.clone(lastLoginDate);
	}
	
	public void setCreationDate(Date creationDate) {
		this.creationDate = CloneUtils.clone(creationDate);
	}

	public Date getCreationDate() {
		return CloneUtils.clone(creationDate);
	}
	
	public Date getLastUpdateDate() {
		return CloneUtils.clone(lastUpdateDate);
	}

	public void setLastUpdateDate(Date lastUpdateDate) {
		this.lastUpdateDate = CloneUtils.clone(lastUpdateDate);
	}

	/**
	 * Fournit la locale préférée de l'utilisation. Il faut utiliser
	 * {@link CoreConfigurer##toAvailableLocale(Locale)} si la locale
	 * préférée de l'utilisateur doit être exploitée pour choisir des traductions.
	 * Cette méthode permet de mapper une locale quelconque (incluant null) sur
	 * une locale qui sera obligatoirement reconnue pas le système (de manière à
	 * avoir un fonctionnement prédictible). 
	 * @return une locale, possiblement null
	 */
	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	@Override
	public int compareTo(CorePerson person) {
		if(this.equals(person)) {
			return 0;
		}
		
		if(DEFAULT_STRING_COLLATOR.compare(this.getLastName(), person.getLastName()) == 0) {
			return DEFAULT_STRING_COLLATOR.compare(this.getFirstName(), person.getFirstName());
		}
		return DEFAULT_STRING_COLLATOR.compare(this.getLastName(), person.getLastName());
	}

	@Override
	public String getDisplayName() {
		return getFullName();
	}

}