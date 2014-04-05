<%@ include file="/WEB-INF/views/include.jsp"%>


			<ul class="pagination">
				<li><a href="${firstUrl}">&lt;&lt;</a></li>
				
				<c:choose>
					<c:when test="${currentIndex ne 1 }">
						<li><a href="${prevUrl}"><span>&lt;</span></a></li>
					</c:when>
					<c:otherwise>
						<li class="disabled"><span>&lt;</span></li>
					</c:otherwise>
				</c:choose>
				
				<c:forEach var="i" begin="1" end="${totalPages}" step="1">
				
					<c:url var="url" value="${baseUrl}/${i}" />
					
					<c:choose>
							<c:when test="${i eq currentIndex}">
								<li class="active"><span>${i} <span class="sr-only">(current)</span></span></li>
							</c:when>
							<c:otherwise>
								<li>
									<a href="${url}">${i}</a>
								</li>
							</c:otherwise>
						</c:choose>
				</c:forEach>
				
				<c:choose>
					<c:when test="${currentIndex ne totalPages }">
						<li><a href="${nextUrl}">&gt;</a></li>
					</c:when>
					<c:otherwise>
						<li class="disabled"><span>&gt;</span></li>
					</c:otherwise>
				</c:choose>

				<li><a href="${lastUrl}">&gt;&gt;</a></li>
	        </ul>
