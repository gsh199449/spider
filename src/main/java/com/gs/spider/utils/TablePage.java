package com.gs.spider.utils;

/**
 * 表格分页工具
 * @author Hokis
 * 
 */
public final class TablePage {
	//总记录数
	private long totalRow;
	//当前页
	private int currentPage;
	//每页显示条数
	private int pageSize;
	//总页数
	private int pageCount;
	//底部显示页码长度
	private int showSize = 5;
	//底部页面范围
	private int[] pageRange;
	//其他参数
	private String otherParam;

	public TablePage(long totalRow, int currentPage, int pageSize) {
		this.totalRow = totalRow;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
	}

	//再次检查参数，计算pageCount
	public void checkAgain() {
		//检查当前页
		if (currentPage < 1) {
			currentPage = 1;
		}
		//判断总记录数
		if (totalRow > 0) {
			//设置总页数
			pageCount = (int) (totalRow / pageSize);
			if (totalRow % pageSize != 0 ) {
				pageCount ++;
			}
			
			if (currentPage > pageCount) {
				currentPage = pageCount;
			}
		}else {
			pageCount = 0;
		}
	}

	/**
	 * 根据总页数和当前页，显示最多5项，尽量以当前页为中心
	 * @return 返回起始和结束位置
	 */
	public int[] getPageRange(){
		int begin = 1,end = pageCount;
		if (pageCount > showSize) {
			if (currentPage - 1 <= 2) {
				end = showSize;
			}else if (currentPage - 1 > 2 && pageCount - currentPage > 2) {
				begin = currentPage - 2;
				end = currentPage + 2;
			}else{
				begin = pageCount - showSize + 1 ;
				end = pageCount;
			}
		}
		pageRange = new int[]{begin,end};
		return pageRange;
	}

	public String getOtherParam() {
		return otherParam;
	}

	public void setOtherParam(String otherParam) {
		this.otherParam = otherParam;
	}

	public long getTotalRow() {
		return totalRow;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageCount() {
		return pageCount;
	}

	public int getShowSize() {
		return showSize;
	}

	
	
}
