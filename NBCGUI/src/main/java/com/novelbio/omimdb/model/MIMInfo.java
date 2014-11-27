package com.novelbio.omimdb.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.novelbio.omimdb.mongorepo.RepoGenemap;
import com.novelbio.omimdb.mongorepo.RepoMIMInfo;
import com.novelbio.springonly.factory.SpringFactory;
@Document(collection = "omimInfo")
public class MIMInfo implements Serializable {

	/** MIM ID */
	@Indexed
	private int mimId;
	/** MIM Title 号 */
	private String mimTitle;
	/** MIM Txt 信息*/
	private String mimTxt;
	/** Other information */
	private String othInfor;
	
	public void setMimId(int mimId) {
		this.mimId = mimId;
	}
	public int getMimId() {
		return mimId;
	}
	public void setMimTitle(String mimTitle) {
		this.mimTitle = mimTitle;
	}
	public String getMimTitle() {
		return mimTitle;
	}
	public void setMimTxt(String mimTxt) {
		this.mimTxt = mimTxt;
	}
	public String getMimTxt() {
		return mimTxt;
	}
	public void setOthInfor(String othInfor) {
		this.othInfor = othInfor;
	}
	public String getOthInfor() {
		return othInfor;
	}
	
	/**
	 * 输入一个list，包含了Omim的一个record，用该record来填充本类
	 * 
	 * *RECORD*
*FIELD* NO
100050
*FIELD* TI
100050 AARSKOG SYNDROME, AUTOSOMAL DOMINANT
*FIELD* TX

DESCRIPTION

Aarskog syndrome is characterized by short stature and facial, limb, and
genital anomalies. One form of the disorder is X-linked (see 305400),
but there is also evidence for autosomal dominant and autosomal
recessive (227330) inheritance (summary by Grier et al., 1983).

CLINICAL FEATURES

Grier et al. (1983) reported father and 2 sons with typical Aarskog
syndrome, including short stature, hypertelorism, and shawl scrotum.
Stretchable skin was present in these patients.

INHERITANCE

Grier et al. (1983) tabulated the findings in 82 previously reported
cases of Aarskog syndrome and noted that X-linked recessive inheritance
was repeatedly suggested. However, their family had father-to-son
transmission, and a family reported by Welch (1974) had affected males
in 3 consecutive generations. Grier et al. (1983) suggested autosomal
dominant inheritance with strong sex-influence and possibly
ascertainment bias resulting from use of the shawl scrotum as a main
criterion.

Van de Vooren et al. (1983) studied a large family in which Aarskog
syndrome was segregating with variable expression in 3 generations and
with male-to-male transmission. Because 3 daughters of affected males
had no features of Aarskog syndrome and 2 sons of an affected male had
several features of the syndrome, van de Vooren et al. (1983) suggested
sex-influenced autosomal dominant inheritance.

*FIELD* RF
1. Grier, R. E.; Farrington, F. H.; Kendig, R.; Mamunes, P.: Autosomal
dominant inheritance of the Aarskog syndrome. Am. J. Med. Genet. 15:
39-46, 1983.

2. van de Vooren, M. J.; Niermeijer, M. F.; Hoogeboom, A. J. M.:
The Aarskog syndrome in a large family, suggestive for autosomal dominant
inheritance. Clin. Genet. 24: 439-445, 1983.

3. Welch, J. P.: Elucidation of a 'new' pleiotropic connective tissue
disorder. Birth Defects Orig. Art. Ser. X(10): 138-146, 1974.

*FIELD* CS

Growth:
   Mild to moderate short stature

Head:
   Normocephaly

Hair:
   Widow's peak

Facies:
   Maxillary hypoplasia;
   Broad nasal bridge;
   Anteverted nostrils;
   Long philtrum;
   Broad upper lip;
   Curved linear dimple below the lower lip

Eyes:
   Hypertelorism;
   Ptosis;
   Down-slanted palpebral fissures;
   Ophthalmoplegia;
   Strabismus;
   Hyperopic astigmatism;
   Large cornea

Ears:
   Floppy ears;
   Lop-ears

Mouth:
   Cleft lip/palate

GU:
   Shawl scrotum;
   Saddle-bag scrotum;
   Cryptorchidism

Limbs:
   Brachydactyly;
   Digital contractures;
   Clinodactyly;
   Mild syndactyly;
   Transverse palmar crease;
   Lymphedema of the feet

Joints:
   Ligamentous laxity;
   Osteochondritis dissecans;
   Proximal finger joint hyperextensibility;
   Flexed distal finger joints;
   Genu recurvatum;
   Flat feet

Skin:
   Stretchable skin

Spine:
   Cervical spine hypermobility;
   Odontoid anomaly

Heme:
   Macrocytic anemia;
   Hemochromatosis

GI:
   Hepatomegaly;
   Portal cirrhosis;
   Imperforate anus;
   Rectoperineal fistula

Pulmonary:
   Interstitial pulmonary disease

Thorax:
   Sternal deformity

Inheritance:
   Sex-influenced autosomal dominant form;
   also X-linked form

*FIELD* CN
Nara Sobreira - updated: 4/22/2013

*FIELD* CD
Victor A. McKusick: 6/4/1986

*FIELD* ED
carol: 04/24/2013
carol: 4/22/2013
carol: 2/16/2011
alopez: 6/3/1997
mimadm: 3/11/1994
carol: 7/7/1993
supermim: 3/16/1992
supermim: 3/20/1990
ddp: 10/26/1989
marie: 3/25/1988

	 * @param lsOmimunit
	 * @return
	 */
	public static MIMInfo getInstanceFromOmimUnit(List<String> lsOmimunit) {
		if (lsOmimunit.isEmpty()) {
			return null;
		}
		MIMInfo mimInfo = new MIMInfo();
		String fieldTitle = "";
		String fieldTxt = "";
		String key;
		String des = "";
		int flag = 0;
		String fieldTitleTmp ="";
		int fieldNumber = 0;
		int otherNumber = 0;
		for (String content : lsOmimunit) {
			if (content.startsWith("*RECORD*")) {
				continue;
			} else if (content.startsWith("*FIELD*")) {	
				if (!(fieldTitle.equals(""))) {
					if (!(fieldTitle.equals("OT"))) {
						if (fieldTxt.length()>10) {
							fieldTxt = fieldTxt.substring(10);
						} else if (fieldTxt.length() ==10) {
							fieldTxt = "";
						}
					}
					if (fieldTitle.equals("NO")) {
						mimInfo.setMimId(Integer.parseInt(fieldTxt.trim()));
					} else if (fieldTitle.equals("TI")) {
						mimInfo.setMimTitle(fieldTxt);
					} else if (fieldTitle.equals("TX")) {
						mimInfo.setMimTxt(fieldTxt);
					}
					if (!(fieldTitle.equals("OT"))) {
						fieldTxt = "";
					}				
				}
				if (flag>2) {
					fieldTitle = "OT";
				} else {
					fieldTitle = content.split("\\s")[1];
					flag++;	
				}
			} else {
				if (fieldTitle.equals("OT")) {
					fieldTxt = fieldTxt.concat(content + " ");
					mimInfo.setOthInfor(fieldTxt);		
				}
			} 
			fieldTxt = fieldTxt.concat(content + " ");
		}	
		
		return mimInfo;
	}
	 private static RepoMIMInfo repo() {
		 return SpringFactory.getBean(RepoMIMInfo.class);
		 
	 }
	 
	 public boolean remove() {
		 try {
			 repo().delete(mimId+"");
		 } catch (Exception e) {
			 return false;
		 }
		 return true;
	 }	 
	 
}


