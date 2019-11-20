## USER
> url: http://oreh.onyah.net:7080

#### 1. 로그인(GET): getClientUser(client_id,password)
> /user/login?id={client_id}&passwd={password}

#### 2. 회원가입(POST): addClientUser(name, client_id, passwd, phone, email)
> /user/login?id={id}&passwd={passwd}&name={name}&phone={phone}&email={email}

#### 3. 카테고리별 가게 이름&대표메뉴 1개 출력(GET): getCategoryShop(category)
> /categories?category={category}

#### 4. 한 가게에 대한 상세 정보 모두 출력(GET): getShopDetail(id)
> /categories/shop?id={shop_id}

#### 5. 한 가게에 대한 상세 메뉴 정보 출력(GET): getMenuwithTimeSale(shop_id, time)
> /categories/menu?id={shop_id}&time={current time: HH}

#### 6. 마이페이지 회원정보 출력(GET): getClientUserDetail(client_id)
> /mypage/user?id={client_id}

#### 7. 마이페이지 비밀번호만 변경(POST): updateClientUser(client_id, new_passwd)
> /mypage/user?id={client_id}&new={new_passwd}

#### 8. 자리 예약 정보 출력(GET): getUserReservationTable(client_id)
> /reservation/table/user?id={client_id}

#### 9. 번호표 예약 정보 출력(GET): getReservationMenu(client_id)
> /reservation/detail?id={client_id}

#### 10. 예약에 필요한 초기 정보 출력(GET): getReservationTable(shop_id, time)
> /reservation/table/remain?id={shop_id}&time={reservation_time}

#### 11. 예약 정보 등록(POST)
##### 11.1) addReservation(classification, client_id, time, shop_id)
> /reservation/add?current={YYMMDDHHmmss}&user={client_id}&time={reservation_time}&shop={shop_id}

##### 11.2) addReservationMenu(classification, client_id, name, count)
> /reservation/add/menu?current={YYMMDDHHmmss}&user={client_id}&shop={shop_id}&menu={menu_name}&count={menu_count}

##### 11.3) addReservationTable(classification, client_id, shop_id, number, count)
> /reservation/add/table?current={YYMMDDHHmmss}&user={client_id}&shop={shop_id}&table={number_of_table}&count={table_count}

#### 12. 예약 정보 삭제(POST/GET): 
##### 12.1) deleteReservationAll(reservation_id, shop_id)
> /reservation/delete?reservation={reservation_id}&shop={shop_id}
##### 12.2) 삭제된 예약 정보 notification:
> /reservation/getDelete

#### 13. 해당 유저가 찜한 가게 리스트 출력(GET): getLikeShop(client_id)
> /like?id={client_id}

#### 14. 찜한 가게 추가(POST): addLikeShop(shop_id, client_id)
> /like/add?shop={shop_id}&user={client_id}

#### 15. 찜한 가게 삭제(POST): deleteLikeShop(shop_id, client_id)
> /like/delete?shop={shop_id}&user={client_id}
