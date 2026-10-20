const $ = (s) => document.querySelector(s);
let students = [], courses = [], results = [];
const message = (text, error=false) => { const el=$('#appMessage'); el.textContent=text; el.classList.toggle('error',error); };
async function api(url, options={}) {
  const response = await fetch(url, {credentials:'same-origin', headers:{'Content-Type':'application/json', ...(options.headers||{})}, ...options});
  if (response.status === 401) { showLogin(); throw new Error('Please log in again.'); }
  if (!response.ok) {
    let detail = `Request failed (${response.status})`;
    try { const data=await response.json(); detail=data.message || detail; } catch {}
    throw new Error(detail);
  }
  if (response.status === 204) return null;
  return response.json();
}
function showLogin(){ $('#loginView').classList.remove('hidden'); $('#appView').classList.add('hidden'); $('#logoutBtn').classList.add('hidden'); }
function showApp(){ $('#loginView').classList.add('hidden'); $('#appView').classList.remove('hidden'); $('#logoutBtn').classList.remove('hidden'); }
$('#loginForm').addEventListener('submit', async e=>{
  e.preventDefault(); $('#loginMessage').textContent='';
  const data=Object.fromEntries(new FormData(e.target));
  try { await api('/api/auth/login',{method:'POST',body:JSON.stringify(data)}); showApp(); await refreshAll(); }
  catch(err){ $('#loginMessage').textContent=err.message; $('#loginMessage').className='message error'; }
});
$('#logoutBtn').addEventListener('click',async()=>{try{await api('/api/auth/logout',{method:'POST'});}catch{} showLogin();});
document.querySelectorAll('.tab').forEach(btn=>btn.addEventListener('click',()=>{
  document.querySelectorAll('.tab').forEach(b=>b.classList.toggle('active',b===btn));
  ['studentsPanel','coursesPanel','resultsPanel'].forEach(id=>$('#'+id).classList.toggle('hidden',id!==btn.dataset.tab));
}));
function esc(v){return String(v??'').replace(/[&<>"']/g,c=>({'&':'&amp;','<':'&lt;','>':'&gt;','"':'&quot;',"'":'&#39;'}[c]));}
async function refreshAll(){
  try {
    [students,courses,results]=await Promise.all([api('/api/students'),api('/api/courses'),api('/api/results')]);
    renderStudents(); renderCourses(); renderResults(); updateCounts(); message('');
  } catch(err){message(err.message,true);}
}
function updateCounts(){ $('#studentCount').textContent=students.length; $('#courseCount').textContent=courses.length; $('#resultCount').textContent=results.length; }
function renderStudents(){
  const q=$('#searchBox').value.toLowerCase();
  const filtered=students.filter(s=>[s.fullName,s.rollNumber,s.department].some(v=>String(v).toLowerCase().includes(q)));
  $('#studentsTable').innerHTML=filtered.map(s=>`<tr><td><b>${esc(s.rollNumber)}</b></td><td>${esc(s.fullName)}</td><td>${esc(s.email)}</td><td>${esc(s.department)}</td><td>Year ${s.yearLevel}</td><td><button class="action edit" data-edit="${s.id}">Edit</button><button class="action delete" data-delete="${s.id}">Delete</button></td></tr>`).join('') || '<tr><td colspan="6">No students found.</td></tr>';
  $('#studentsTable').querySelectorAll('[data-edit]').forEach(b=>b.onclick=()=>editStudent(Number(b.dataset.edit)));
  $('#studentsTable').querySelectorAll('[data-delete]').forEach(b=>b.onclick=()=>deleteStudent(Number(b.dataset.delete)));
}
$('#searchBox').addEventListener('input',renderStudents);
function editStudent(id){
  const s=students.find(x=>x.id===id); if(!s)return;
  const f=$('#studentForm'); Object.entries(s).forEach(([k,v])=>{if(f.elements[k])f.elements[k].value=v??'';});
  $('#saveStudentBtn').textContent='Save changes'; f.scrollIntoView({behavior:'smooth',block:'center'});
}
$('#cancelEditBtn').onclick=()=>{ $('#studentForm').reset(); $('#studentForm').elements.id.value=''; $('#saveStudentBtn').textContent='Add student'; };
$('#studentForm').addEventListener('submit',async e=>{
  e.preventDefault(); const f=e.target; const data=Object.fromEntries(new FormData(f)); const id=data.id; delete data.id; data.yearLevel=Number(data.yearLevel);
  try {
    await api(id?`/api/students/${id}`:'/api/students',{method:id?'PUT':'POST',body:JSON.stringify(data)});
    f.reset(); f.elements.id.value=''; $('#saveStudentBtn').textContent='Add student';
    await refreshAll(); message(id?'Student updated.':'Student added.');
  }catch(err){message(err.message,true);}
});
async function deleteStudent(id){
  const s=students.find(x=>x.id===id);
  if(!confirm(`Delete ${s?.fullName || 'this student'}? Related results will also be deleted.`))return;
  try{await api(`/api/students/${id}`,{method:'DELETE'});await refreshAll();message('Student deleted.');}catch(err){message(err.message,true);}
}
function renderCourses(){
  $('#coursesTable').innerHTML=courses.map(c=>`<tr><td><b>${esc(c.courseCode)}</b></td><td>${esc(c.courseName)}</td><td>${c.credits}</td><td><button class="action delete" data-course-delete="${c.id}">Delete</button></td></tr>`).join('')||'<tr><td colspan="4">No courses yet.</td></tr>';
  $('#coursesTable').querySelectorAll('[data-course-delete]').forEach(b=>b.onclick=async()=>{
    if(!confirm('Delete this course?'))return;
    try{await api(`/api/courses/${b.dataset.courseDelete}`,{method:'DELETE'});await refreshAll();message('Course deleted.');}catch(err){message(err.message,true);}
  });
  $('#resultStudent').innerHTML='<option value="">Select student</option>'+students.map(s=>`<option value="${s.id}">${esc(s.rollNumber)} — ${esc(s.fullName)}</option>`).join('');
  $('#resultCourse').innerHTML='<option value="">Select course</option>'+courses.map(c=>`<option value="${c.id}">${esc(c.courseCode)} — ${esc(c.courseName)}</option>`).join('');
}
$('#courseForm').addEventListener('submit',async e=>{
  e.preventDefault();const data=Object.fromEntries(new FormData(e.target));data.credits=Number(data.credits);
  try{await api('/api/courses',{method:'POST',body:JSON.stringify(data)});e.target.reset();await refreshAll();message('Course added.');}catch(err){message(err.message,true);}
});
function renderResults(){
  $('#resultsTable').innerHTML=results.map(r=>`<tr><td>${esc(r.student?.fullName||'')}</td><td>${esc(r.course?.courseName||'')}</td><td>${r.semester}</td><td><b>${Number(r.marks).toFixed(2)}%</b></td><td><span class="status">${r.marks>=40?'Pass':'Needs improvement'}</span></td></tr>`).join('')||'<tr><td colspan="5">No results recorded yet.</td></tr>';
}
$('#resultForm').addEventListener('submit',async e=>{
  e.preventDefault();const data=Object.fromEntries(new FormData(e.target));data.studentId=Number(data.studentId);data.courseId=Number(data.courseId);data.marks=Number(data.marks);data.semester=Number(data.semester);
  try{await api('/api/results',{method:'POST',body:JSON.stringify(data)});e.target.reset();await refreshAll();message('Result saved.');}catch(err){message(err.message,true);}
});
// If an existing session is still active, open the dashboard automatically.
(async()=>{try{await api('/api/students');showApp();await refreshAll();}catch{showLogin();}})();
