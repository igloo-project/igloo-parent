package fr.openwide.mpxj.example;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import net.sf.mpxj.Duration;
import net.sf.mpxj.Priority;
import net.sf.mpxj.ProjectFile;
import net.sf.mpxj.ProjectHeader;
import net.sf.mpxj.RelationType;
import net.sf.mpxj.Resource;
import net.sf.mpxj.ResourceAssignment;
import net.sf.mpxj.Task;
import net.sf.mpxj.TimeUnit;
import net.sf.mpxj.mspdi.MSPDIWriter;
import net.sf.mpxj.writer.ProjectWriter;

/**
 * Exemple de génération de fichier MS Project (format MSPDI).
 * 
 * Cet exemple s'inspire des exemples founis par MPXJ (net.sf.mpxj.sample.MpxjCreate).
 * Cet exemple s'inspire aussi du projet : http://code.google.com/p/redmine-connect/downloads/detail?name=redmine_connect_r53.zip&can=1&q=
 * 
 * Pour la création de la dépendance Maven MPXJ voir le fichier Readme.txt
 * 
 * @author Open Wide
 */
public class MSProjectGeneration {

	protected SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	public void write() throws IOException, ParseException {
		ProjectFile project = new ProjectFile();

		//Génération automatique des Ids des tâches
		project.setAutoTaskID(true);
		project.setAutoTaskUniqueID(true);
		
		//Génération automatique de Ids des ressources
		project.setAutoResourceID(true);
		project.setAutoResourceUniqueID(true);
		
		//Génération automatique des numéro de niveau hierarchique des tâches
		project.setAutoOutlineLevel(true);
		project.setAutoOutlineNumber(true);
		
		//Génération automatique des labels WBS
		project.setAutoWBS(true);
		
		//Génération automatique des Ids des calendars
		project.setAutoCalendarUniqueID(true);

		//Ajout d'un calendrier par défaut (OBLIGATOIRE)
		project.addDefaultBaseCalendar();

		/*
		 * Définition de la date de début du projet (OLIGATOIRE)
		 * 
		 * Microsoft Project réinitilise la date début des premières tâches à
		 * cette date. Il faut donc lui donner la même valeur que la date de
		 * début des première tâches.
		 */
		ProjectHeader header = project.getProjectHeader();
		header.setStartDate(dateFormat.parse("28/05/2010"));

		initTasks(project);

		writeProject("src/main/resources/my_mspdi.xml", project);
	}

	private static void writeProject(String fileName, ProjectFile project) throws IOException {
		ProjectWriter writer = new MSPDIWriter();
		writer.write(project, fileName);
	}
	
	private static Duration getDurationInDays(double duration) {
		return Duration.getInstance(duration, TimeUnit.DAYS);
	}
	
	private void initTasks(ProjectFile project) throws ParseException {
		Resource resource1 = project.addResource();
		resource1.setName("J. Gonzalez");

		Resource resource2 = project.addResource();
		resource2.setName("G. Smet");

		Task task1 = project.addTask();
		task1.setName("Produit A");
		task1.setStart(dateFormat.parse("28/05/2010"));
		task1.setDuration(getDurationInDays(15));
		task1.setPercentageComplete(50.0);
		task1.setPriority(Priority.getInstance(Priority.HIGH));

		Task milestone11 = task1.addTask();
		milestone11.setName("Gate1");
		milestone11.setMilestone(true);
		milestone11.setStart(dateFormat.parse("28/05/2010"));
		milestone11.setDuration(getDurationInDays(0));

		Task milestone12 = task1.addTask();
		milestone12.setName("Gate2");
		milestone12.setMilestone(true);
		milestone12.setStart(dateFormat.parse("10/06/2010"));
		milestone12.setDuration(getDurationInDays(0));

		Task milestone13 = task1.addTask();
		milestone13.setName("Gate3");
		milestone13.setMilestone(true);
		milestone13.setStart(dateFormat.parse("12/06/2010"));
		milestone13.setDuration(getDurationInDays(0));

		Task subTask11 = task1.addTask();
		subTask11.setName("Conception");
		subTask11.setStart(dateFormat.parse("02/06/2010"));
		subTask11.setDuration(getDurationInDays(2));
		subTask11.setPriority(Priority.getInstance(Priority.HIGH));

		Task subTask12 = task1.addTask();
		subTask12.setName("Industrialisation");
		subTask12.setStart(dateFormat.parse("04/06/2010"));
		subTask12.setDuration(getDurationInDays(6));
		subTask12.setPriority(Priority.getInstance(Priority.MEDIUM));

		Task task2 = project.addTask();
		task2.setName("Building Block A");
		task2.setStart(dateFormat.parse("28/05/2010"));
		task2.setDuration(getDurationInDays(10));
		task1.setPriority(Priority.getInstance(Priority.MEDIUM));

		Task subtask21 = task2.addTask();
		subtask21.setName("Conception");
		subtask21.setStart(dateFormat.parse("03/06/2010"));
		subtask21.setDuration(getDurationInDays(1));

		Task subtask22 = task2.addTask();
		subtask22.setName("Industrialisation");
		subtask22.setStart(dateFormat.parse("04/06/2010"));
		subtask22.setDuration(getDurationInDays(3));
		subtask22.setPriority(Priority.getInstance(Priority.MEDIUM));

		Task milestone21 = task2.addTask();
		milestone21.setName("Product (TRL 9)");
		milestone21.setMilestone(true);
		milestone21.setStart(dateFormat.parse("29/05/2010"));
		milestone21.setDuration(getDurationInDays(0));

		Task milestone22 = task2.addTask();
		milestone22.setName("Concept (TRL 4)");
		milestone22.setMilestone(true);
		milestone22.setStart(dateFormat.parse("02/06/2010"));
		milestone22.setDuration(getDurationInDays(0));

		Task milestone23 = task2.addTask();
		milestone23.setName("Prototype (TRL 6)");
		milestone23.setMilestone(true);
		milestone23.setStart(dateFormat.parse("07/06/2010"));
		milestone23.setDuration(getDurationInDays(0));

		// Définition du/des prédécesseurs
		milestone13.addPredecessor(milestone22, RelationType.FINISH_START, null);
		milestone13.addPredecessor(milestone23, RelationType.FINISH_START, null);
		
		// Assignation des ressources pour les taches
		ResourceAssignment assignment1 = task1.addResourceAssignment(resource1);
		ResourceAssignment assignment2 = task2.addResourceAssignment(resource2);

		/*
		 * La tâche 1 est en cours de réalisation, donc si on lui assigne une
		 * ressource il faut OBLIGATOIREMENT spécifier la temps de travail total
		 * et le temps de travail effectué /*
		 */
		assignment1.setWork(Duration.getInstance(80, TimeUnit.HOURS));
		assignment1.setActualWork(Duration.getInstance(40, TimeUnit.HOURS));
		
		assignment1.setRemainingWork(Duration.getInstance(40, TimeUnit.HOURS));
		assignment2.setRemainingWork(Duration.getInstance(80, TimeUnit.HOURS));
		assignment1.setStart(dateFormat.parse("28/05/2010"));
		assignment2.setStart(dateFormat.parse("28/05/2010"));
	}
}
