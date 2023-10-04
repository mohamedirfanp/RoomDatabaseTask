package com.example.roomdatabasetask

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.overscroll
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.roomdatabasetask.ui.theme.RoomDatabaseTaskTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomDatabaseTaskTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val scope = rememberCoroutineScope()
                    val database = MyDatabase.getDatabase(applicationContext)
                    val myDao = database.myDao();

//                    var insertData = UserModel(userName = "", bloodGroup = "")

                    var userName by remember {
                        mutableStateOf("")
                    }

                    var userBloodType by remember {
                        mutableStateOf("")
                    }

                    var isExpanded by remember {
                        mutableStateOf(false)
                    }

                    var searchBloodType by remember {
                        mutableStateOf("")
                    }

                    var isExpandedSearch by remember {
                        mutableStateOf(false)
                    }

                    var userData by remember {
                        mutableStateOf<List<UserModel>>(emptyList())
                    }

                    var isSearch by remember {
                        mutableStateOf(false)
                    }

                    var requestedUserName by remember {
                        mutableStateOf("")
                    }

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {

                            Text(text = "Insert the Data", fontSize = 30.sp)
                            TextField(
                                value = userName, onValueChange = {
                                    (userName) = it
                                },
                                placeholder = ({
                                    Text(text = "Enter username")
                                })
                            )

                            BloodGroupDropdown(
                                userBloodType = userBloodType,
                                onBloodGroupSelected = { bloodGroup ->
                                    userBloodType = bloodGroup
                                },
                                isExpanded = isExpanded,
                                onExpandedChange = { expanded ->
                                    isExpanded = expanded
                                }
                            )

                            Button(onClick = {
                                scope.launch {
                                    try {
                                        myDao.d(
                                            UserModel(
                                                userName = userName,
                                                bloodGroup = userBloodType
                                            )
                                        )

                                        userBloodType = ""
                                        isExpanded = false
                                        userName = ""

                                    } catch (ex: Exception) {
                                        println("cancelled")
                                    }
                                }
                            }) {
                                Text(text = "Insert")
                            }

                            Text(
                                text = "Search By BloodGroup",
                                modifier = Modifier.padding(10.dp),
                                fontSize = 30.sp
                            )

                            BloodGroupDropdown(
                                userBloodType = searchBloodType,
                                onBloodGroupSelected = { bloodGroup ->
                                    searchBloodType = bloodGroup
                                },
                                isExpanded = isExpandedSearch,
                                onExpandedChange = { expanded ->
                                    isExpandedSearch = expanded
                                }
                            )

                            Row{
                                Button(onClick = {
                                    scope.launch {
                                        try {
                                            userData = myDao.getUsers(searchBloodType)
                                            isSearch = true
                                        } catch (ex: Exception) {
                                            println("cancelled")
                                        }
                                    }

                                }) {
                                    Text(text = "Search")
                                }
                                Spacer(modifier = Modifier.padding(5.dp))

                                if(isSearch)
                                {
                                    Button(onClick = {
                                        isSearch = !isSearch
                                        searchBloodType = ""

                                    }) {
                                        Text(text = "Hide")
                                    }
                                }
                            }



                            if(isSearch)
                            {
                                UserListTable(userData)
                            }


                            Text(text = "Delete User By UserName",
                                modifier = Modifier.padding(10.dp),
                                fontSize = 30.sp)

                            TextField(value = requestedUserName, onValueChange = {
                                requestedUserName = it
                            },
                                placeholder = ({
                                    Text(text = "Enter UserName")
                                }))

                            Button(onClick = {
                                scope.launch {
                                    try {
                                        myDao.deleteUser(requestedUserName = requestedUserName)

                                        requestedUserName = ""
                                    }
                                    catch (ex:Exception)
                                    {
                                        println("Cancelled")
                                    }
                                }
                            }) {
                                Text(text = "Delete")
                            }

                        }
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BloodGroupDropdown(
    userBloodType: String,
    onBloodGroupSelected: (String) -> Unit,
    isExpanded: Boolean,
    onExpandedChange: (Boolean) -> Unit
) {
    ExposedDropdownMenuBox(
        expanded = isExpanded, onExpandedChange = onExpandedChange,
    ) {
        TextField(
            value = userBloodType,
            onValueChange = {},
            readOnly = true,
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            placeholder = {
                Text(text = "Please select your blood group")
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = {
                onExpandedChange(false)
            }
        ) {
            // Dropdown menu items go here
            DropdownMenuItem(
                text = {
                    Text(text = "A+")
                },
                onClick = {
                    onBloodGroupSelected("A+")
                    onExpandedChange(false)
                }
            )
            DropdownMenuItem(
                text = {
                    Text(text = "A-")
                },
                onClick = {
                    onBloodGroupSelected("A-")
                    onExpandedChange(false)
                }
            )
            DropdownMenuItem(
                text = {
                    Text(text = "B+")
                },
                onClick = {
                    onBloodGroupSelected("B+")
                    onExpandedChange(false)
                }
            )
            DropdownMenuItem(
                text = {
                    Text(text = "B-")
                },
                onClick = {
                    onBloodGroupSelected("B-")
                    onExpandedChange(false)
                }
            )
            DropdownMenuItem(
                text = {
                    Text(text = "O")
                },
                onClick = {
                    onBloodGroupSelected("O")
                    onExpandedChange(false)
                }
            )
            DropdownMenuItem(
                text = {
                    Text(text = "AB")
                },
                onClick = {
                    onBloodGroupSelected("AB")
                    onExpandedChange(false)
                }
            )

        }
    }
}
@Composable
fun UserListTable(userList: List<UserModel>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),

    ){
        item {
            // Table header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary)
            ) {
                TableCell(text = "User Name")
                TableCell(text = "Blood Group")
            }
        }
       items(userList!!){
           Row(
               modifier = Modifier
                   .padding(end = 100.dp)
                   .fillMaxWidth()
           ) {
               TableCell(text = it.userName)
               Spacer(modifier = Modifier.padding(40.dp))
               TableCell(text = it.bloodGroup)
           }

       }
    }
}


@Composable
fun TableCell(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .padding(8.dp)
    )
}




