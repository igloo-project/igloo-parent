package fr.openwide.core.basicapp.web.application.administration.export;

import java.util.Collection;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCreationHelper;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.wicket.Component;
import org.apache.wicket.markup.repeater.data.IDataProvider;

import com.google.common.collect.ImmutableList;

import fr.openwide.core.basicapp.core.business.user.model.User;
import fr.openwide.core.basicapp.core.business.user.model.UserBinding;
import fr.openwide.core.export.excel.ColumnInformation;
import fr.openwide.core.jpa.util.HibernateUtils;
import fr.openwide.core.wicket.more.export.excel.AbstractSimpleExcelTableExport;

public class UserExcelTableExport extends AbstractSimpleExcelTableExport {
	
	private static final String SHEET_NAME_RESOURCE_KEY = "administration.export.excel.sheetName";
	
	private final Collection<ColumnInformation> columns = ImmutableList.of(
			new ColumnInformation("business.user.userName"),
			new ColumnInformation("business.user.lastName"),
			new ColumnInformation("business.user.firstName"),
			new ColumnInformation("business.user.email"),
			new ColumnInformation("business.user.active"),
			new ColumnInformation("business.user.creationDate"),
			new ColumnInformation("business.user.lastUpdateDate"),
			new ColumnInformation("business.user.lastLoginDate")
	);

	public UserExcelTableExport(Component component) {
		super(component);
	}

	public Workbook generate(IDataProvider<? extends User> dataProvider) {
		Sheet sheet = createSheet(localize(SHEET_NAME_RESOURCE_KEY));
		
		int rowIndex = 0;
		
		// Headers
		addHeadersToSheet(sheet, rowIndex, columns);
		++rowIndex;
		
		// Rows
		Iterator<? extends User> iterator = dataProvider.iterator(0, Integer.MAX_VALUE);
		while (iterator.hasNext()) {
			Row currentRow = sheet.createRow(rowIndex);
			
			populateRow(currentRow, HibernateUtils.unwrap(iterator.next()));
			
			++rowIndex;
		}
		
		finalizeSheet(sheet, columns);
		
		return workbook;
	}

	private void populateRow(Row row, User user) {
		UserBinding binding = new UserBinding(user);
		
		int columnIndex = 0;
		
		addTextCell(row, columnIndex++, binding.userName().getSafely());
		addTextCell(row, columnIndex++, binding.lastName().getSafely());
		addTextCell(row, columnIndex++, binding.firstName().getSafely());
		
		XSSFCreationHelper helper= (XSSFCreationHelper) workbook.getCreationHelper();
		XSSFHyperlink emailLink = helper.createHyperlink(Hyperlink.LINK_EMAIL);
		String emailAddress = binding.email().getSafely();
		emailLink.setAddress("mailto:" + emailAddress);
		addLinkToCell(addTextCell(row, columnIndex++, emailAddress), emailLink);
		
		if (binding.active().getSafely()) {
			addTextCell(row, columnIndex++, "Oui");
		}
		else {
			addTextCell(row, columnIndex++, "Non");
		}
		
		if (binding.creationDate().getSafely() != null) {
			addDateCell(row, columnIndex++, binding.creationDate().getSafely());
		} else {
			addTextCell(row, columnIndex++, "");
		}
		
		if (binding.lastUpdateDate().getSafely() != null) {
			addDateCell(row, columnIndex++, binding.lastUpdateDate().getSafely());
		} else {
			addTextCell(row, columnIndex++, "");
		}
		
		if (binding.lastLoginDate().getSafely() != null) {
			addDateCell(row, columnIndex++, binding.lastLoginDate().getSafely());
		} else {
			addTextCell(row, columnIndex++, "");
		}
	}
	
}

