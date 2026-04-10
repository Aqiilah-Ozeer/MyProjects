// script.js (FINAL CHECKED VERSION)

document.addEventListener('DOMContentLoaded', () => {
    const mainButtonsContainer = document.querySelector('.main-category-buttons');
    const subButtonsContainer = document.getElementById('sub-category-buttons-container');
    const petGrid = document.getElementById('pet-grid');

    // Mappings for Main Category (Animal) to Subtypes (Breed) based on your database inserts.
    const subtypeMap = {
        'Cat': [ 'Siamese', 'Maine Coon', 'Bengal', 'Persian', 'Russian Blue', 'Tabby', 'Calico', 'Scottish Fold'], 
        'Dog': [ 'Golden Retriever', 'Beagle', 'Shih Tzu', 'German Shepherd', 'Labrador Retriever', 'Pomeranian', 'Rottweiler', 'Border Collie', 'Boxer', 'Doberman', 'Husky'], 
        'Rabbit': ['Mini Lop', 'Dutch', 'Holland Lop', 'Angora', 'Rex', 'Lionhead'], 
        'Bird': ['Cockatiel', 'Lovebird', 'Budgerigar', 'Parakeet', 'Canary'], 
        '': [] 
    };

    let activeMain = ''; // Starts blank for "All Pets"
    let activeSub = '';

    /**
     * Fetches and displays the pets using AJAX with XML response.
     * Uses XMLHttpRequest to call the PHP endpoint that returns XML data,
     * then parses the XML DOM to dynamically build the pet listing cards.
     */
    function loadPets() {
        petGrid.innerHTML = '<div class="loading-message"><p>Fetching results...</p></div>';

        // Construct the URL for the XML endpoint
        const url = `fetch_pets_xml.php?main=${encodeURIComponent(activeMain)}&sub=${encodeURIComponent(activeSub)}`;

        // Create XMLHttpRequest object for AJAX with XML response
        const xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.responseType = 'document'; // Expect XML document response
        xhr.overrideMimeType('application/xml');

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4) {
                if (xhr.status === 200) {
                    try {
                        const xmlDoc = xhr.responseXML;

                        if (!xmlDoc || xmlDoc.getElementsByTagName('parsererror').length > 0) {
                            petGrid.innerHTML = '<p class="no-results">Error: Invalid XML response from server. Please try again.</p>';
                            console.error('XML Parse Error:', xmlDoc ? xmlDoc.documentElement.textContent : 'No XML');
                            return;
                        }

                        // Check for error elements in XML response
                        const errors = xmlDoc.getElementsByTagName('error');
                        if (errors.length > 0) {
                            petGrid.innerHTML = '<p class="no-results">Server Error: ' + errors[0].textContent + '</p>';
                            console.error('Server Error:', errors[0].textContent);
                            return;
                        }

                        // Parse the XML and build HTML
                        const pets = xmlDoc.getElementsByTagName('pet');

                        if (pets.length === 0) {
                            petGrid.innerHTML = '<p class="no-results">😔 No pets match your current filter criteria.</p>';
                            return;
                        }

                        let html = '';
                        for (let i = 0; i < pets.length; i++) {
                            const pet = pets[i];
                            const id = pet.getElementsByTagName('id')[0].textContent;
                            const name = pet.getElementsByTagName('name')[0].textContent;
                            const breed = pet.getElementsByTagName('breed')[0].textContent;
                            const status = pet.getElementsByTagName('status')[0].textContent;
                            const image = pet.getElementsByTagName('image')[0].textContent;
                            const statusClass = (status === 'Adopted') ? 'status-adopted' : 'status-available';

                            html += `
                            <div class="grid-card">
                              <a href="petinfo.php?id=${id}">
                                <div class="card-image-wrapper">
                                    <img src="${image}" alt="${name}">
                                    <span class="pet-status ${statusClass}">${status}</span>
                            </div>
                            <div class="card-details">
                                <h3>${name}</h3>
                                <p class="species-breed">${breed}</p>
                            </div>
                          </a>
                        </div>`;
                    }
                    petGrid.innerHTML = html;
                } catch (e) {
                    petGrid.innerHTML = '<p class="no-results">Error processing XML: ' + e.message + '</p>';
                    console.error('XML Processing Error:', e);
                }
            } else {
                    console.error('AJAX Error: Server responded with status', xhr.status);
                    petGrid.innerHTML = '<p class="no-results">Error: Failed to load pet listings. Check F12 console for details.</p>';
            }
            }
        };

        xhr.onerror = function () {
            console.error('AJAX request failed (network error).');
            petGrid.innerHTML = '<p class="no-results">Error: Network error. Failed to load pet listings.</p>';
        };

        xhr.send();
    }

    /**
     * Updates the subcategory buttons based on the active main category.
     */
    function updateSubCategoryButtons() {
        subButtonsContainer.innerHTML = '<h2>Filter by Subtype/Breed:</h2>';
        const subtypes = subtypeMap[activeMain];

        if (subtypes && subtypes.length > 0) {
            const allSubButton = document.createElement('button');
            allSubButton.textContent = `All ${activeMain} Breeds`;
            allSubButton.classList.add('sub-filter', 'active');
            allSubButton.dataset.sub = '';
            subButtonsContainer.appendChild(allSubButton);

            subtypes.forEach(sub => {
                const button = document.createElement('button');
                button.textContent = sub;
                button.classList.add('sub-filter');
                button.dataset.sub = sub;
                subButtonsContainer.appendChild(button);
            });
        } else {
            const initialText = document.createElement('p');
            initialText.classList.add('initial-sub-text');
            initialText.textContent = activeMain ? `No specific subtypes for ${activeMain}.` : 'Select a species to view available breeds/subtypes.';
            subButtonsContainer.appendChild(initialText);
        }
        
        // Ensure the "All" button for the subtype group is initially active if subtypes exist
        const initialActiveSubButton = subButtonsContainer.querySelector('.sub-filter[data-sub=""]');
        if (initialActiveSubButton) {
            initialActiveSubButton.classList.add('active');
        }
    }

    // --- Live Search using AJAX with XML Response ---
    const searchInput = document.getElementById('pet-search-input');
    const searchDropdown = document.getElementById('search-results-dropdown');
    let searchTimer = null; // For debouncing keystrokes

    /**
     * Performs a live search using XMLHttpRequest with XML response.
     * Sends the search query to fetch_pets_xml.php and parses the returned
     * XML to display matching results in a dropdown below the search bar.
     */
    function liveSearch(query) {
        if (query.length < 1) {
            searchDropdown.innerHTML = '';
            searchDropdown.style.display = 'none';
            return;
        }

        const url = `fetch_pets_xml.php?search=${encodeURIComponent(query)}`;

        // AJAX call using XMLHttpRequest with XML response
        const xhr = new XMLHttpRequest();
        xhr.open('GET', url, true);
        xhr.responseType = 'document';
        xhr.overrideMimeType('application/xml');

        xhr.onreadystatechange = function () {
            if (xhr.readyState === 4 && xhr.status === 200) {
                try {
                    const xmlDoc = xhr.responseXML;

                    if (!xmlDoc || xmlDoc.getElementsByTagName('parsererror').length > 0) {
                        searchDropdown.innerHTML = '<div class="search-item">Error parsing results.</div>';
                        searchDropdown.style.display = 'block';
                        return;
                    }

                    // Check for error elements
                    const errors = xmlDoc.getElementsByTagName('error');
                    if (errors.length > 0) {
                        searchDropdown.innerHTML = '<div class="search-item">Server Error: ' + errors[0].textContent + '</div>';
                        searchDropdown.style.display = 'block';
                        return;
                    }

                    const pets = xmlDoc.getElementsByTagName('pet');

                    if (pets.length === 0) {
                        searchDropdown.innerHTML = '<div class="search-item no-match">No pets found for "' + query + '"</div>';
                        searchDropdown.style.display = 'block';
                        return;
                    }

                    let html = '';
                    for (let i = 0; i < pets.length; i++) {
                        const pet = pets[i];
                        const id = pet.getElementsByTagName('id')[0].textContent;
                        const name = pet.getElementsByTagName('name')[0].textContent;
                        const breed = pet.getElementsByTagName('breed')[0].textContent;
                        const animal = pet.getElementsByTagName('animal')[0].textContent;
                        const image = pet.getElementsByTagName('image')[0].textContent;
                        const status = pet.getElementsByTagName('status')[0].textContent;

                        html += `
                        <a href="petinfo.php?id=${id}" class="search-item">
                            <img src="${image}" alt="${name}">
                        <div class="search-item-info">
                            <strong>${name}</strong>
                            <span>${breed} · ${animal} · ${status}</span>
                        </div>
                    </a>`;
                }
                searchDropdown.innerHTML = html;
                searchDropdown.style.display = 'block';
                } catch (e) {
                    searchDropdown.innerHTML = '<div class="search-item">Error: ' + e.message + '</div>';
                    searchDropdown.style.display = 'block';
                    console.error('Search Error:', e);
                }
            }
        };

        xhr.send();
    }

    // Debounce: wait 300ms after the user stops typing before sending the AJAX request
    searchInput.addEventListener('input', function () {
        clearTimeout(searchTimer);
        searchTimer = setTimeout(() => {
            liveSearch(this.value.trim());
        }, 300);
    });

    // Hide dropdown when clicking outside
    document.addEventListener('click', function (e) {
        if (!searchInput.contains(e.target) && !searchDropdown.contains(e.target)) {
            searchDropdown.style.display = 'none';
        }
    });

    // Clear search and reload all pets when search is emptied
    searchInput.addEventListener('input', function () {
        if (this.value.trim() === '') {
            searchDropdown.style.display = 'none';
        }
    });

    // --- Event Listeners ---
    mainButtonsContainer.addEventListener('click', (e) => {
        if (e.target.matches('.main-filter')) {
            document.querySelectorAll('.main-filter').forEach(btn => btn.classList.remove('active'));
            e.target.classList.add('active');

            activeMain = e.target.dataset.main;
            activeSub = '';

            updateSubCategoryButtons();
            loadPets();
        }
    });

    subButtonsContainer.addEventListener('click', (e) => {
        if (e.target.matches('.sub-filter')) {
            document.querySelectorAll('.sub-filter').forEach(btn => btn.classList.remove('active'));
            e.target.classList.add('active');

            activeSub = e.target.dataset.sub;
            loadPets();
        }
    });

    // Initial setup to load "All Pets" grid immediately
    updateSubCategoryButtons();
    loadPets(); 
});